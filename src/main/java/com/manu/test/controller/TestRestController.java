package com.manu.test.controller;

import com.manu.test.bean.StreamingRangeResponseBody;
import com.manu.test.quartz.TestQuartzJob;
import com.manu.test.service.TestTransactionService;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.UUID;

/**
 * @author emmanuel.mura
 *
 */
@CrossOrigin(origins = "http://localhost:8091")
@RestController
public class TestRestController {

    public static final Logger logger = LoggerFactory.getLogger(TestRestController.class);

    @Autowired
    private Scheduler scheduler;

    @Autowired
    private TestTransactionService testTransactionService;

    private static final String FILE_PATH = "/sample.mp4";
    private final int chunk_size = 1024 * 1024 * 2; // 2 MB chunks

    @GetMapping("/secure")
    public ResponseEntity<Object> secure() {
        return ResponseEntity.status(HttpStatus.OK).body("secure");
    }

    @GetMapping("/unsecure")
    public ResponseEntity<Object> unsecure() {
        return ResponseEntity.status(HttpStatus.OK).body("unsecure");
    }

    @GetMapping("/internal")
    public ResponseEntity<Object> internal() {
        return ResponseEntity.status(HttpStatus.OK).body("internal");
    }

    @GetMapping("/test/transaction")
    public ResponseEntity<Object> testTransaction() {

        logger.info("transaction");

        try {
            testTransactionService.createCityAndCountry();
            return new ResponseEntity<>("testTransaction ok", HttpStatus.OK);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/test/quartz")
    public ResponseEntity<Object> testQuartz() {

        logger.info("testQuartz");

        try {
            LocalDateTime localDateTime = LocalDateTime.now();
            ZoneId zoneId = ZoneId.systemDefault();
            ZonedDateTime dateTime = ZonedDateTime.of(localDateTime, zoneId);
//            if(dateTime.isBefore(ZonedDateTime.now())) {
//                return ResponseEntity.badRequest().body("dateTime must be after current time");
//            }

            logger.info("Date: {}", dateTime);
            ZonedDateTime startDateTime = dateTime.plusSeconds(30);
            logger.info("Start date: {}", startDateTime);

            JobDetail jobDetail = buildJobDetail("bonjour la France !!");
            Trigger trigger = buildJobTrigger(jobDetail, startDateTime);
            scheduler.scheduleJob(jobDetail, trigger);

            return new ResponseEntity<>("testQuartz ok", HttpStatus.OK);

        } catch (SchedulerException ex) {
            logger.error("Error scheduling test job", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error in quartz scheduler");
        }

    }

    private JobDetail buildJobDetail(String message) {
        JobDataMap jobDataMap = new JobDataMap();

        jobDataMap.put("message", message);

        return JobBuilder.newJob(TestQuartzJob.class)
                .withIdentity(UUID.randomUUID().toString(), "test-jobs")
                .withDescription("Test Job")
                .usingJobData(jobDataMap)
                .storeDurably()
                .build();
    }

    private Trigger buildJobTrigger(JobDetail jobDetail, ZonedDateTime startAt) {
        return TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withIdentity(jobDetail.getKey().getName(), "test-triggers")
                .withDescription("Test Trigger")
                .startAt(Date.from(startAt.toInstant()))
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withMisfireHandlingInstructionFireNow())
                .build();
    }

    // for clients to check whether the server supports range / partial content requests
//    @HEAD
    @RequestMapping(value = "/test/stream", method = RequestMethod.HEAD, produces = "video/avi")
    //    @Produces("video/avi")
    public ResponseEntity<?> header() {
        logger.info("@HEAD request received");

        URL url = this.getClass().getResource( FILE_PATH );
        File file = new File( url.getFile() );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentLength(file.length());
        headers.set("Accept-Ranges", "bytes");
        return new ResponseEntity<>(headers, HttpStatus.PARTIAL_CONTENT);

//        return Response.ok()
//        		.status( Response.Status.PARTIAL_CONTENT )
//        		.header( HttpHeaders.CONTENT_LENGTH, file.length() )
//        		.header( "Accept-Ranges", "bytes" )
//        		.build();
    }

    //    @GET
//    @Path("/stream")
//    @Produces("video/mp4")
    @GetMapping(value = "/test/stream") // produces = "video/avi"
    public ResponseEntity<StreamingResponseBody> stream(@RequestHeader(value = "Range", required = false) String range,
                                                        @RequestHeader("Accept") String accept,
                                                        @RequestHeader("Accept-Language") String acceptLanguage,
                                                        @RequestHeader("User-Agent") String userAgent) {

        logger.debug("stream request");
        logger.debug("range: {}", range);
//		logger.debug("accept: {}", accept);
//		logger.debug("acceptLanguage: {}", acceptLanguage);
        logger.debug("userAgent: {}", userAgent);

        try {
            URL url = this.getClass().getResource( FILE_PATH );

            if (url == null) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            File file = new File( url.getFile() );
            return buildStream( file, range );
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * @param asset Media file
     * @param range range header
     * @return Streaming output
     * @throws Exception IOException if an error occurs in streaming.
     */
    private ResponseEntity<StreamingResponseBody> buildStream( final File asset, final String range ) throws Exception {

        try {
            // range not requested: firefox does not send range headers
            if ( range == null ) {
                logger.info("Request does not contain a range parameter!");

//	            StreamingOutput streamer = output -> {
//	                try ( FileChannel inputChannel = new FileInputStream( asset ).getChannel();
//	                	  WritableByteChannel outputChannel = Channels.newChannel( output ) ) {
//
//	                    inputChannel.transferTo( 0, inputChannel.size(), outputChannel );
//	                }
//	                catch( IOException io ) {
//	                	logger.error( io.getMessage(), io );
//	                }
//	            };

                StreamingResponseBody body = output -> {
                    try (FileChannel inputChannel = new FileInputStream(asset).getChannel(); WritableByteChannel outputChannel = Channels.newChannel(output)) {
                        inputChannel.transferTo(0, inputChannel.size(), outputChannel);
                    }
                };

//	            final InputStream videoFileStream = new FileInputStream(asset);
//	            StreamingResponseBody body = new StreamingResponseBody() {
//					@Override
//					public void writeTo(OutputStream os) throws IOException {
//						readAndWrite(videoFileStream, os);
//					}
//				};
//			    InputStreamResource inputStreamResource = new InputStreamResource(videoFileStream, asset.getName());

                HttpHeaders headers = new HttpHeaders();
                headers.setContentLength(asset.length());
                return new ResponseEntity<>(body, headers, HttpStatus.OK);

                //            return Response.ok( streamer )
                //            		.status( Response.Status.OK )
                //            		.header( HttpHeaders.CONTENT_LENGTH, asset.length() )
                //            		.build();
            }

            logger.info( "Requested Range: " + range );

            String[] ranges = range.split( "=" )[1].split( "-" );

            int from = Integer.parseInt( ranges[0] );

            // Chunk media if the range upper bound is unspecified
            int to = chunk_size + from;

            if ( to >= asset.length() ) {
                to = (int) ( asset.length() - 1 );
            }

            // uncomment to let the client decide the upper bound
            // (we want to send 2 MB chunks all the time)
	        /*
	          Chunk media if the range upper bound is unspecified. Chrome, Opera sends "bytes=0-"
	         */
            if ( ranges.length == 2 ) {
                to = Integer.parseInt( ranges[1] );
            }

            final String responseRange = String.format( "bytes=%d-%d", from, to );
            final String responseContentRange = String.format( "bytes %d-%d/%d", from, to, asset.length() );

            logger.info( "Response Range: " + responseRange);
            logger.info( "Response Content-Range: " + responseContentRange + "\n");

            final RandomAccessFile raf = new RandomAccessFile( asset, "r" );
            raf.seek( from );

            final int len = to - from + 1;
            //        final MediaStreamer mediaStreamer = new MediaStreamer( len, raf );
            final StreamingRangeResponseBody body = new StreamingRangeResponseBody(len, raf);

//	        final InputStream videoFileStream = new FileInputStream(asset);
//            StreamingResponseBody body = new StreamingResponseBody() {
//				@Override
//				public void writeTo(OutputStream os) throws IOException {
//					readAndWrite(videoFileStream, os);
//				}
//			};

            // TODO : FF + edge not working
            HttpHeaders headers = new HttpHeaders();
            headers.set("Accept-Ranges", "bytes");
            headers.set("Content-Range", responseContentRange);
            headers.set("Range", responseRange);
            headers.setContentLength(body.getLength());
            headers.setLastModified(asset.lastModified());
            return new ResponseEntity<>(body, headers, HttpStatus.PARTIAL_CONTENT);
//	        return body;

            //        return Response.ok( mediaStreamer )
            //				.status(Response.Status.PARTIAL_CONTENT )
            //                .header( "Accept-Ranges", "bytes" )
            //                .header( "Content-Range", responseRange )
            //                .header( HttpHeaders.CONTENT_LENGTH, mediaStreamer. getLenth() )
            //                .header( HttpHeaders.LAST_MODIFIED, new Date( asset.lastModified() ) )
            //                .build();

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
//    		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            return null;
        }
    }

    private void readAndWrite(final InputStream is, OutputStream os) throws IOException {
        byte[] data = new byte[2048];
        int read = 0;
        int i = 0;
        while ((read = is.read(data)) > 0) {
            os.write(data, 0, read);
            logger.debug("buffer Number " + i++);
        }
        os.flush();
    }

}
