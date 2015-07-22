<%@ page import="com.fortune.util.AppConfigurator" %><%@ page
        import="com.fortune.util.CommandRunner" %><%@ page
        import="com.fortune.server.message.ServerMessager" %><%@ page
        import="java.util.Map" %><%@ page
        import="java.util.HashMap" %><%@ page
        import="org.dom4j.Element" %><%@ page
        import="com.fortune.util.XmlUtils" %><%@ page
        import="java.util.List" %><%@ page
        import="org.dom4j.Node" %><%@ page
        import="java.util.ArrayList" %><%@ page
        import="com.fortune.util.JsonUtils" %><%@ page
        import="org.apache.log4j.Logger" %>
<%@ page import="java.util.concurrent.TimeoutException" %>
<%--
  Created by IntelliJ IDEA.
  User: xjliu
  Date: 2015/7/3
  Time: 16:04
  To change this template use File | Settings | File Templates.
--%><%@ page contentType="text/html;charset=UTF-8" language="java" %><%
    String addr = request.getParameter("addr");
    AppConfigurator appConfigurator = AppConfigurator.getInstance();
    String fromClient = request.getParameter("fromClient");
    Map<String,Object> result = null;
    if("true".equals(fromClient)){
        String serverUrl = appConfigurator.getConfig("system.live.scan.serverUrl","http://192.168.1.88:18080/interface/scanLive.jsp");
        ServerMessager messager = new ServerMessager();
        logger.debug("准备访问远端服务："+serverUrl+",参数：addr="+addr);
        out.print(messager.postToHost(serverUrl, "addr=" + addr));
        return;
    }else{
        if("true".equals(request.getParameter("test"))){
            logger.debug("测试访问，直接返回固定的xml数据！");
            result = getResult(xml);
        }else{
            String commandLine = appConfigurator.getConfig("system.live.scan.cmdLine","/usr/local/bin/ffprobe -show_programs -print_format xml -i 'udp://%addr%?localaddr=192.168.2.88'");
            commandLine = commandLine.replace("%addr%",addr);
            CommandRunner runner = new CommandRunner();
            logger.debug("准备执行命令行："+commandLine);
            String xmlResult ="";
            try {
                List<String> runResult =(runner.runCommand(commandLine, null, 10000, null));
                if(runResult!=null&&runResult.size()>0){
                    for(String s:runResult){
                        xmlResult+=s+"\r\n";
                    }
                }
            } catch (TimeoutException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if("".equals(xmlResult)){
                logger.error("命令行执行结果异常，没有任何返回结果："+commandLine);
            }else{
                logger.debug("命令行返回数据了：\n"+xmlResult);
                result = getResult(xmlResult);
            }
        }

    }
    if(result!=null){
        result.put("addr",addr);
    }
    out.print(JsonUtils.getJsonString(result));
%><%!
    Logger logger = Logger.getLogger("com.fortune.rms.jsp.scanLive.jsp");
    String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<ffprobe>\n" +
            "    <programs>\n" +
            "        <program program_id=\"133\" program_num=\"133\" nb_streams=\"2\" pmt_pid=\"1330\" pcr_pid=\"1331\" start_pts=\"80734994733\" start_time=\"80734.994733\">\n" +
            "            <tag key=\"service_name\" value=\"RBC TV\"/>\n" +
            "            <tag key=\"service_provider\" value=\"\"/>\n" +
            "            <streams>\n" +
            "                <stream index=\"5\" codec_name=\"mpeg2video\" codec_long_name=\"MPEG-2 video\" profile=\"Main\" codec_type=\"video\" codec_time_base=\"1/50\" codec_tag_string=\"[2][0][0][0]\" codec_tag=\"0x0002\" width=\"720\" height=\"576\" has_b_frames=\"1\" sample_aspect_ratio=\"16:15\" display_aspect_ratio=\"4:3\" pix_fmt=\"yuv420p\" level=\"8\" color_range=\"tv\" timecode=\"06:16:35:07\" id=\"0x533\" r_frame_rate=\"25/1\" avg_frame_rate=\"25/1\" time_base=\"1/90000\" start_pts=\"7266211474\" start_time=\"80735.683044\" max_bit_rate=\"15000000\">\n" +
            "                    <disposition default=\"0\" dub=\"0\" original=\"0\" comment=\"0\" lyrics=\"0\" karaoke=\"0\" forced=\"0\" hearing_impaired=\"0\" visual_impaired=\"0\" clean_effects=\"0\" attached_pic=\"0\"/>\n" +
            "                </stream>\n" +
            "                <stream index=\"7\" codec_name=\"mp1\" codec_long_name=\"MP1 (MPEG audio layer 1)\" codec_type=\"audio\" codec_time_base=\"1/48000\" codec_tag_string=\"[0][0][0][0]\" codec_tag=\"0x0000\" sample_fmt=\"s16p\" sample_rate=\"48000\" channels=\"1\" channel_layout=\"mono\" bits_per_sample=\"0\" id=\"0x534\" r_frame_rate=\"0/0\" avg_frame_rate=\"0/0\" time_base=\"1/90000\" start_pts=\"7266149526\" start_time=\"80734.994733\" bit_rate=\"128000\">\n" +
            "                    <disposition default=\"0\" dub=\"0\" original=\"0\" comment=\"0\" lyrics=\"0\" karaoke=\"0\" forced=\"0\" hearing_impaired=\"0\" visual_impaired=\"0\" clean_effects=\"0\" attached_pic=\"0\"/>\n" +
            "                </stream>\n" +
            "            </streams>\n" +
            "        </program>\n" +
            "        <program program_id=\"121\" program_num=\"121\" nb_streams=\"2\" pmt_pid=\"1210\" pcr_pid=\"1211\" start_pts=\"20936207089\" start_time=\"20936.207089\">\n" +
            "            <tag key=\"service_name\" value=\"CCTV 4A\"/>\n" +
            "            <tag key=\"service_provider\" value=\"\"/>\n" +
            "            <streams>\n" +
            "                <stream index=\"23\" codec_name=\"mpeg2video\" codec_long_name=\"MPEG-2 video\" profile=\"Main\" codec_type=\"video\" codec_time_base=\"1/50\" codec_tag_string=\"[2][0][0][0]\" codec_tag=\"0x0002\" width=\"720\" height=\"576\" has_b_frames=\"1\" sample_aspect_ratio=\"16:15\" display_aspect_ratio=\"4:3\" pix_fmt=\"yuv420p\" level=\"8\" color_range=\"tv\" timecode=\"15:22:38:03\" id=\"0x4bb\" r_frame_rate=\"25/1\" avg_frame_rate=\"25/1\" time_base=\"1/90000\" start_pts=\"1884311053\" start_time=\"20936.789478\" max_bit_rate=\"15000000\">\n" +
            "                    <disposition default=\"0\" dub=\"0\" original=\"0\" comment=\"0\" lyrics=\"0\" karaoke=\"0\" forced=\"0\" hearing_impaired=\"0\" visual_impaired=\"0\" clean_effects=\"0\" attached_pic=\"0\"/>\n" +
            "                </stream>\n" +
            "                <stream index=\"20\" codec_name=\"mp2\" codec_long_name=\"MP2 (MPEG audio layer 2)\" codec_type=\"audio\" codec_time_base=\"1/48000\" codec_tag_string=\"[4][0][0][0]\" codec_tag=\"0x0004\" sample_fmt=\"s16p\" sample_rate=\"48000\" channels=\"2\" channel_layout=\"stereo\" bits_per_sample=\"0\" id=\"0x4bc\" r_frame_rate=\"0/0\" avg_frame_rate=\"0/0\" time_base=\"1/90000\" start_pts=\"1884258638\" start_time=\"20936.207089\" bit_rate=\"250171\">\n" +
            "                    <disposition default=\"0\" dub=\"0\" original=\"0\" comment=\"0\" lyrics=\"0\" karaoke=\"0\" forced=\"0\" hearing_impaired=\"0\" visual_impaired=\"0\" clean_effects=\"1\" attached_pic=\"0\"/>\n" +
            "                </stream>\n" +
            "            </streams>\n" +
            "        </program>\n" +
            "        <program program_id=\"122\" program_num=\"122\" nb_streams=\"2\" pmt_pid=\"1220\" pcr_pid=\"1221\" start_pts=\"20936273744\" start_time=\"20936.273744\">\n" +
            "            <tag key=\"service_name\" value=\"CCTV-NEWS\"/>\n" +
            "            <tag key=\"service_provider\" value=\"\"/>\n" +
            "            <streams>\n" +
            "                <stream index=\"9\" codec_name=\"mpeg2video\" codec_long_name=\"MPEG-2 video\" profile=\"Main\" codec_type=\"video\" codec_time_base=\"1/50\" codec_tag_string=\"[2][0][0][0]\" codec_tag=\"0x0002\" width=\"720\" height=\"576\" has_b_frames=\"1\" sample_aspect_ratio=\"16:15\" display_aspect_ratio=\"4:3\" pix_fmt=\"yuv420p\" level=\"8\" color_range=\"tv\" timecode=\"15:22:35:13\" id=\"0x4c5\" r_frame_rate=\"25/1\" avg_frame_rate=\"25/1\" time_base=\"1/90000\" start_pts=\"1884332656\" start_time=\"20937.029511\" max_bit_rate=\"15000000\">\n" +
            "                    <disposition default=\"0\" dub=\"0\" original=\"0\" comment=\"0\" lyrics=\"0\" karaoke=\"0\" forced=\"0\" hearing_impaired=\"0\" visual_impaired=\"0\" clean_effects=\"0\" attached_pic=\"0\"/>\n" +
            "                </stream>\n" +
            "                <stream index=\"25\" codec_name=\"mp2\" codec_long_name=\"MP2 (MPEG audio layer 2)\" codec_type=\"audio\" codec_time_base=\"1/48000\" codec_tag_string=\"[4][0][0][0]\" codec_tag=\"0x0004\" sample_fmt=\"s16p\" sample_rate=\"48000\" channels=\"2\" channel_layout=\"stereo\" bits_per_sample=\"0\" id=\"0x4c6\" r_frame_rate=\"0/0\" avg_frame_rate=\"0/0\" time_base=\"1/90000\" start_pts=\"1884264637\" start_time=\"20936.273744\" bit_rate=\"249071\">\n" +
            "                    <disposition default=\"0\" dub=\"0\" original=\"0\" comment=\"0\" lyrics=\"0\" karaoke=\"0\" forced=\"0\" hearing_impaired=\"0\" visual_impaired=\"0\" clean_effects=\"1\" attached_pic=\"0\"/>\n" +
            "                </stream>\n" +
            "            </streams>\n" +
            "        </program>\n" +
            "        <program program_id=\"123\" program_num=\"123\" nb_streams=\"2\" pmt_pid=\"1230\" pcr_pid=\"1231\" start_pts=\"20936232100\" start_time=\"20936.232100\">\n" +
            "            <tag key=\"service_name\" value=\"CCTV-F\"/>\n" +
            "            <tag key=\"service_provider\" value=\"\"/>\n" +
            "            <streams>\n" +
            "                <stream index=\"26\" codec_name=\"mpeg2video\" codec_long_name=\"MPEG-2 video\" profile=\"Main\" codec_type=\"video\" codec_time_base=\"1/50\" codec_tag_string=\"[2][0][0][0]\" codec_tag=\"0x0002\" width=\"720\" height=\"576\" has_b_frames=\"1\" sample_aspect_ratio=\"16:15\" display_aspect_ratio=\"4:3\" pix_fmt=\"yuv420p\" level=\"8\" color_range=\"tv\" timecode=\"15:22:33:00\" id=\"0x4cf\" r_frame_rate=\"25/1\" avg_frame_rate=\"25/1\" time_base=\"1/90000\" start_pts=\"1884347046\" start_time=\"20937.189400\" max_bit_rate=\"15000000\">\n" +
            "                    <disposition default=\"0\" dub=\"0\" original=\"0\" comment=\"0\" lyrics=\"0\" karaoke=\"0\" forced=\"0\" hearing_impaired=\"0\" visual_impaired=\"0\" clean_effects=\"0\" attached_pic=\"0\"/>\n" +
            "                </stream>\n" +
            "                <stream index=\"24\" codec_name=\"mp2\" codec_long_name=\"MP2 (MPEG audio layer 2)\" codec_type=\"audio\" codec_time_base=\"1/48000\" codec_tag_string=\"[4][0][0][0]\" codec_tag=\"0x0004\" sample_fmt=\"s16p\" sample_rate=\"48000\" channels=\"2\" channel_layout=\"stereo\" bits_per_sample=\"0\" id=\"0x4d0\" r_frame_rate=\"0/0\" avg_frame_rate=\"0/0\" time_base=\"1/90000\" start_pts=\"1884260889\" start_time=\"20936.232100\" bit_rate=\"250171\">\n" +
            "                    <disposition default=\"0\" dub=\"0\" original=\"0\" comment=\"0\" lyrics=\"0\" karaoke=\"0\" forced=\"0\" hearing_impaired=\"0\" visual_impaired=\"0\" clean_effects=\"1\" attached_pic=\"0\"/>\n" +
            "                </stream>\n" +
            "            </streams>\n" +
            "        </program>\n" +
            "        <program program_id=\"124\" program_num=\"124\" nb_streams=\"2\" pmt_pid=\"1240\" pcr_pid=\"1241\" start_pts=\"116379938833\" start_time=\"116379.938833\">\n" +
            "            <tag key=\"service_name\" value=\"CCTV-E\"/>\n" +
            "            <tag key=\"service_provider\" value=\"\"/>\n" +
            "            <streams>\n" +
            "                <stream index=\"2\" codec_name=\"mpeg2video\" codec_long_name=\"MPEG-2 video\" profile=\"Main\" codec_type=\"video\" codec_time_base=\"1/50\" codec_tag_string=\"[2][0][0][0]\" codec_tag=\"0x0002\" width=\"720\" height=\"576\" has_b_frames=\"1\" sample_aspect_ratio=\"16:15\" display_aspect_ratio=\"4:3\" pix_fmt=\"yuv420p\" level=\"8\" color_range=\"tv\" timecode=\"15:22:30:13\" id=\"0x4d9\" r_frame_rate=\"25/1\" avg_frame_rate=\"25/1\" time_base=\"1/90000\" start_pts=\"10474267204\" start_time=\"116380.746711\" max_bit_rate=\"15000000\">\n" +
            "                    <disposition default=\"0\" dub=\"0\" original=\"0\" comment=\"0\" lyrics=\"0\" karaoke=\"0\" forced=\"0\" hearing_impaired=\"0\" visual_impaired=\"0\" clean_effects=\"0\" attached_pic=\"0\"/>\n" +
            "                </stream>\n" +
            "                <stream index=\"3\" codec_name=\"mp2\" codec_long_name=\"MP2 (MPEG audio layer 2)\" codec_type=\"audio\" codec_time_base=\"1/48000\" codec_tag_string=\"[4][0][0][0]\" codec_tag=\"0x0004\" sample_fmt=\"s16p\" sample_rate=\"48000\" channels=\"2\" channel_layout=\"stereo\" bits_per_sample=\"0\" id=\"0x4da\" r_frame_rate=\"0/0\" avg_frame_rate=\"0/0\" time_base=\"1/90000\" start_pts=\"10474194495\" start_time=\"116379.938833\" bit_rate=\"250171\">\n" +
            "                    <disposition default=\"0\" dub=\"0\" original=\"0\" comment=\"0\" lyrics=\"0\" karaoke=\"0\" forced=\"0\" hearing_impaired=\"0\" visual_impaired=\"0\" clean_effects=\"1\" attached_pic=\"0\"/>\n" +
            "                </stream>\n" +
            "            </streams>\n" +
            "        </program>\n" +
            "        <program program_id=\"125\" program_num=\"125\" nb_streams=\"2\" pmt_pid=\"1250\" pcr_pid=\"1251\" start_pts=\"20936196467\" start_time=\"20936.196467\">\n" +
            "            <tag key=\"service_name\" value=\"CCTV-R\"/>\n" +
            "            <tag key=\"service_provider\" value=\"\"/>\n" +
            "            <streams>\n" +
            "                <stream index=\"11\" codec_name=\"mpeg2video\" codec_long_name=\"MPEG-2 video\" profile=\"Main\" codec_type=\"video\" codec_time_base=\"1/50\" codec_tag_string=\"[2][0][0][0]\" codec_tag=\"0x0002\" width=\"720\" height=\"576\" has_b_frames=\"1\" sample_aspect_ratio=\"16:15\" display_aspect_ratio=\"4:3\" pix_fmt=\"yuv420p\" level=\"8\" color_range=\"tv\" timecode=\"15:22:28:04\" id=\"0x4e3\" r_frame_rate=\"25/1\" avg_frame_rate=\"25/1\" time_base=\"1/90000\" start_pts=\"1884329047\" start_time=\"20936.989411\" max_bit_rate=\"15000000\">\n" +
            "                    <disposition default=\"0\" dub=\"0\" original=\"0\" comment=\"0\" lyrics=\"0\" karaoke=\"0\" forced=\"0\" hearing_impaired=\"0\" visual_impaired=\"0\" clean_effects=\"0\" attached_pic=\"0\"/>\n" +
            "                </stream>\n" +
            "                <stream index=\"12\" codec_name=\"mp2\" codec_long_name=\"MP2 (MPEG audio layer 2)\" codec_type=\"audio\" codec_time_base=\"1/48000\" codec_tag_string=\"[4][0][0][0]\" codec_tag=\"0x0004\" sample_fmt=\"s16p\" sample_rate=\"48000\" channels=\"2\" channel_layout=\"stereo\" bits_per_sample=\"0\" id=\"0x4e4\" r_frame_rate=\"0/0\" avg_frame_rate=\"0/0\" time_base=\"1/90000\" start_pts=\"1884257682\" start_time=\"20936.196467\" bit_rate=\"250036\">\n" +
            "                    <disposition default=\"0\" dub=\"0\" original=\"0\" comment=\"0\" lyrics=\"0\" karaoke=\"0\" forced=\"0\" hearing_impaired=\"0\" visual_impaired=\"0\" clean_effects=\"1\" attached_pic=\"0\"/>\n" +
            "                </stream>\n" +
            "            </streams>\n" +
            "        </program>\n" +
            "        <program program_id=\"126\" program_num=\"126\" nb_streams=\"2\" pmt_pid=\"1260\" pcr_pid=\"1261\" start_pts=\"20936269700\" start_time=\"20936.269700\">\n" +
            "            <tag key=\"service_name\" value=\"CCTV-A\"/>\n" +
            "            <tag key=\"service_provider\" value=\"\"/>\n" +
            "            <streams>\n" +
            "                <stream index=\"10\" codec_name=\"mpeg2video\" codec_long_name=\"MPEG-2 video\" profile=\"Main\" codec_type=\"video\" codec_time_base=\"1/50\" codec_tag_string=\"[2][0][0][0]\" codec_tag=\"0x0002\" width=\"720\" height=\"576\" has_b_frames=\"1\" sample_aspect_ratio=\"16:15\" display_aspect_ratio=\"4:3\" pix_fmt=\"yuv420p\" level=\"8\" color_range=\"tv\" timecode=\"15:22:26:14\" id=\"0x4ed\" r_frame_rate=\"25/1\" avg_frame_rate=\"25/1\" time_base=\"1/90000\" start_pts=\"10474224042\" start_time=\"116380.267133\" max_bit_rate=\"15000000\">\n" +
            "                    <disposition default=\"0\" dub=\"0\" original=\"0\" comment=\"0\" lyrics=\"0\" karaoke=\"0\" forced=\"0\" hearing_impaired=\"0\" visual_impaired=\"0\" clean_effects=\"0\" attached_pic=\"0\"/>\n" +
            "                </stream>\n" +
            "                <stream index=\"17\" codec_name=\"mp2\" codec_long_name=\"MP2 (MPEG audio layer 2)\" codec_type=\"audio\" codec_time_base=\"1/48000\" codec_tag_string=\"[4][0][0][0]\" codec_tag=\"0x0004\" sample_fmt=\"s16p\" sample_rate=\"48000\" channels=\"2\" channel_layout=\"stereo\" bits_per_sample=\"0\" id=\"0x4ee\" r_frame_rate=\"0/0\" avg_frame_rate=\"0/0\" time_base=\"1/90000\" start_pts=\"1884264273\" start_time=\"20936.269700\" bit_rate=\"248879\">\n" +
            "                    <disposition default=\"0\" dub=\"0\" original=\"0\" comment=\"0\" lyrics=\"0\" karaoke=\"0\" forced=\"0\" hearing_impaired=\"0\" visual_impaired=\"0\" clean_effects=\"0\" attached_pic=\"0\"/>\n" +
            "                </stream>\n" +
            "            </streams>\n" +
            "        </program>\n" +
            "        <program program_id=\"127\" program_num=\"127\" nb_streams=\"2\" pmt_pid=\"1270\" pcr_pid=\"1271\" start_pts=\"116380031522\" start_time=\"116380.031522\">\n" +
            "            <tag key=\"service_name\" value=\"CCTV-9\"/>\n" +
            "            <tag key=\"service_provider\" value=\"\"/>\n" +
            "            <streams>\n" +
            "                <stream index=\"0\" codec_name=\"mpeg2video\" codec_long_name=\"MPEG-2 video\" profile=\"Main\" codec_type=\"video\" codec_time_base=\"1/50\" codec_tag_string=\"[2][0][0][0]\" codec_tag=\"0x0002\" width=\"720\" height=\"576\" has_b_frames=\"1\" sample_aspect_ratio=\"16:15\" display_aspect_ratio=\"4:3\" pix_fmt=\"yuv420p\" level=\"8\" color_range=\"tv\" timecode=\"15:22:24:05\" id=\"0x4f7\" r_frame_rate=\"25/1\" avg_frame_rate=\"25/1\" time_base=\"1/90000\" start_pts=\"10474256442\" start_time=\"116380.627133\" max_bit_rate=\"15000000\">\n" +
            "                    <disposition default=\"0\" dub=\"0\" original=\"0\" comment=\"0\" lyrics=\"0\" karaoke=\"0\" forced=\"0\" hearing_impaired=\"0\" visual_impaired=\"0\" clean_effects=\"0\" attached_pic=\"0\"/>\n" +
            "                </stream>\n" +
            "                <stream index=\"1\" codec_name=\"mp2\" codec_long_name=\"MP2 (MPEG audio layer 2)\" codec_type=\"audio\" codec_time_base=\"1/48000\" codec_tag_string=\"[4][0][0][0]\" codec_tag=\"0x0004\" sample_fmt=\"s16p\" sample_rate=\"48000\" channels=\"2\" channel_layout=\"stereo\" bits_per_sample=\"0\" id=\"0x4f8\" r_frame_rate=\"0/0\" avg_frame_rate=\"0/0\" time_base=\"1/90000\" start_pts=\"10474202837\" start_time=\"116380.031522\" bit_rate=\"248879\">\n" +
            "                    <disposition default=\"0\" dub=\"0\" original=\"0\" comment=\"0\" lyrics=\"0\" karaoke=\"0\" forced=\"0\" hearing_impaired=\"0\" visual_impaired=\"0\" clean_effects=\"1\" attached_pic=\"0\"/>\n" +
            "                </stream>\n" +
            "            </streams>\n" +
            "        </program>\n" +
            "        <program program_id=\"128\" program_num=\"128\" nb_streams=\"2\" pmt_pid=\"1280\" pcr_pid=\"1281\" start_pts=\"37454464400\" start_time=\"37454.464400\">\n" +
            "            <tag key=\"service_name\" value=\"CCTV-1\"/>\n" +
            "            <tag key=\"service_provider\" value=\"\"/>\n" +
            "            <streams>\n" +
            "                <stream index=\"13\" codec_name=\"mpeg2video\" codec_long_name=\"MPEG-2 video\" profile=\"Main\" codec_type=\"video\" codec_time_base=\"1/50\" codec_tag_string=\"[2][0][0][0]\" codec_tag=\"0x0002\" width=\"720\" height=\"576\" has_b_frames=\"1\" sample_aspect_ratio=\"16:15\" display_aspect_ratio=\"4:3\" pix_fmt=\"yuv420p\" level=\"8\" color_range=\"tv\" timecode=\"19:03:42:09\" id=\"0x501\" r_frame_rate=\"25/1\" avg_frame_rate=\"25/1\" time_base=\"1/90000\" start_pts=\"3370901796\" start_time=\"37454.464400\" max_bit_rate=\"15000000\">\n" +
            "                    <disposition default=\"0\" dub=\"0\" original=\"0\" comment=\"0\" lyrics=\"0\" karaoke=\"0\" forced=\"0\" hearing_impaired=\"0\" visual_impaired=\"0\" clean_effects=\"0\" attached_pic=\"0\"/>\n" +
            "                </stream>\n" +
            "                <stream index=\"8\" codec_name=\"mp1\" codec_long_name=\"MP1 (MPEG audio layer 1)\" codec_type=\"audio\" codec_time_base=\"1/48000\" codec_tag_string=\"[4][0][0][0]\" codec_tag=\"0x0004\" sample_fmt=\"s16p\" sample_rate=\"48000\" channels=\"2\" channel_layout=\"stereo\" bits_per_sample=\"0\" id=\"0x502\" r_frame_rate=\"0/0\" avg_frame_rate=\"0/0\" time_base=\"1/90000\" start_pts=\"11960775242\" start_time=\"132897.502689\" bit_rate=\"256000\">\n" +
            "                    <disposition default=\"0\" dub=\"0\" original=\"0\" comment=\"0\" lyrics=\"0\" karaoke=\"0\" forced=\"0\" hearing_impaired=\"0\" visual_impaired=\"0\" clean_effects=\"0\" attached_pic=\"0\"/>\n" +
            "                </stream>\n" +
            "            </streams>\n" +
            "        </program>\n" +
            "        <program program_id=\"129\" program_num=\"129\" nb_streams=\"2\" pmt_pid=\"1290\" pcr_pid=\"1291\" start_pts=\"8527576222\" start_time=\"8527.576222\">\n" +
            "            <tag key=\"service_name\" value=\"CCTV-2\"/>\n" +
            "            <tag key=\"service_provider\" value=\"\"/>\n" +
            "            <streams>\n" +
            "                <stream index=\"22\" codec_name=\"mpeg2video\" codec_long_name=\"MPEG-2 video\" profile=\"Main\" codec_type=\"video\" codec_time_base=\"1/50\" codec_tag_string=\"[2][0][0][0]\" codec_tag=\"0x0002\" width=\"720\" height=\"576\" has_b_frames=\"1\" sample_aspect_ratio=\"16:15\" display_aspect_ratio=\"4:3\" pix_fmt=\"yuv420p\" level=\"8\" color_range=\"tv\" timecode=\"14:54:37:13\" id=\"0x50b\" r_frame_rate=\"25/1\" avg_frame_rate=\"25/1\" time_base=\"1/90000\" start_pts=\"767509579\" start_time=\"8527.884211\" max_bit_rate=\"15000000\">\n" +
            "                    <disposition default=\"0\" dub=\"0\" original=\"0\" comment=\"0\" lyrics=\"0\" karaoke=\"0\" forced=\"0\" hearing_impaired=\"0\" visual_impaired=\"0\" clean_effects=\"0\" attached_pic=\"0\"/>\n" +
            "                </stream>\n" +
            "                <stream index=\"19\" codec_name=\"mp2\" codec_long_name=\"MP2 (MPEG audio layer 2)\" codec_type=\"audio\" codec_time_base=\"1/48000\" codec_tag_string=\"[4][0][0][0]\" codec_tag=\"0x0004\" sample_fmt=\"s16p\" sample_rate=\"48000\" channels=\"2\" channel_layout=\"stereo\" bits_per_sample=\"0\" id=\"0x50c\" r_frame_rate=\"0/0\" avg_frame_rate=\"0/0\" time_base=\"1/90000\" start_pts=\"767481860\" start_time=\"8527.576222\" bit_rate=\"250171\">\n" +
            "                    <disposition default=\"0\" dub=\"0\" original=\"0\" comment=\"0\" lyrics=\"0\" karaoke=\"0\" forced=\"0\" hearing_impaired=\"0\" visual_impaired=\"0\" clean_effects=\"0\" attached_pic=\"0\"/>\n" +
            "                </stream>\n" +
            "            </streams>\n" +
            "        </program>\n" +
            "        <program program_id=\"136\" program_num=\"136\" nb_streams=\"2\" pmt_pid=\"1360\" pcr_pid=\"1361\" start_pts=\"82359147944\" start_time=\"82359.147944\">\n" +
            "            <tag key=\"service_name\" value=\"TVB8\"/>\n" +
            "            <tag key=\"service_provider\" value=\"\"/>\n" +
            "            <streams>\n" +
            "                <stream index=\"14\" codec_name=\"mpeg2video\" codec_long_name=\"MPEG-2 video\" profile=\"Main\" codec_type=\"video\" codec_time_base=\"1/50\" codec_tag_string=\"[2][0][0][0]\" codec_tag=\"0x0002\" width=\"720\" height=\"576\" has_b_frames=\"1\" sample_aspect_ratio=\"16:15\" display_aspect_ratio=\"4:3\" pix_fmt=\"yuv420p\" level=\"8\" color_range=\"tv\" timecode=\"00:00:00:00\" id=\"0x551\" r_frame_rate=\"25/1\" avg_frame_rate=\"25/1\" time_base=\"1/90000\" start_pts=\"7412369244\" start_time=\"82359.658267\" max_bit_rate=\"15000000\">\n" +
            "                    <disposition default=\"0\" dub=\"0\" original=\"0\" comment=\"0\" lyrics=\"0\" karaoke=\"0\" forced=\"0\" hearing_impaired=\"0\" visual_impaired=\"0\" clean_effects=\"0\" attached_pic=\"0\"/>\n" +
            "                </stream>\n" +
            "                <stream index=\"15\" codec_name=\"mp2\" codec_long_name=\"MP2 (MPEG audio layer 2)\" codec_type=\"audio\" codec_time_base=\"1/48000\" codec_tag_string=\"[3][0][0][0]\" codec_tag=\"0x0003\" sample_fmt=\"s16p\" sample_rate=\"48000\" channels=\"2\" channel_layout=\"stereo\" bits_per_sample=\"0\" id=\"0x552\" r_frame_rate=\"0/0\" avg_frame_rate=\"0/0\" time_base=\"1/90000\" start_pts=\"7412323315\" start_time=\"82359.147944\" bit_rate=\"124435\">\n" +
            "                    <disposition default=\"0\" dub=\"0\" original=\"0\" comment=\"0\" lyrics=\"0\" karaoke=\"0\" forced=\"0\" hearing_impaired=\"0\" visual_impaired=\"0\" clean_effects=\"0\" attached_pic=\"0\"/>\n" +
            "                </stream>\n" +
            "            </streams>\n" +
            "        </program>\n" +
            "        <program program_id=\"135\" program_num=\"135\" nb_streams=\"2\" pmt_pid=\"1350\" pcr_pid=\"1351\" start_pts=\"3984512322\" start_time=\"3984.512322\">\n" +
            "            <tag key=\"service_name\" value=\"world  fashion\"/>\n" +
            "            <tag key=\"service_provider\" value=\"\"/>\n" +
            "            <streams>\n" +
            "                <stream index=\"4\" codec_name=\"mpeg2video\" codec_long_name=\"MPEG-2 video\" profile=\"Main\" codec_type=\"video\" codec_time_base=\"1/50\" codec_tag_string=\"[2][0][0][0]\" codec_tag=\"0x0002\" width=\"720\" height=\"576\" has_b_frames=\"1\" sample_aspect_ratio=\"64:45\" display_aspect_ratio=\"16:9\" pix_fmt=\"yuv420p\" level=\"8\" color_range=\"tv\" timecode=\"00:00:00:00\" id=\"0x547\" r_frame_rate=\"25/1\" avg_frame_rate=\"25/1\" time_base=\"1/90000\" start_pts=\"8948594523\" start_time=\"99428.828033\" max_bit_rate=\"15000000\">\n" +
            "                    <disposition default=\"0\" dub=\"0\" original=\"0\" comment=\"0\" lyrics=\"0\" karaoke=\"0\" forced=\"0\" hearing_impaired=\"0\" visual_impaired=\"0\" clean_effects=\"0\" attached_pic=\"0\"/>\n" +
            "                </stream>\n" +
            "                <stream index=\"16\" codec_name=\"mp2\" codec_long_name=\"MP2 (MPEG audio layer 2)\" codec_type=\"audio\" codec_time_base=\"1/48000\" codec_tag_string=\"[3][0][0][0]\" codec_tag=\"0x0003\" sample_fmt=\"s16p\" sample_rate=\"48000\" channels=\"2\" channel_layout=\"stereo\" bits_per_sample=\"0\" id=\"0x548\" r_frame_rate=\"0/0\" avg_frame_rate=\"0/0\" time_base=\"1/90000\" start_pts=\"358606109\" start_time=\"3984.512322\" bit_rate=\"124435\">\n" +
            "                    <disposition default=\"0\" dub=\"0\" original=\"0\" comment=\"0\" lyrics=\"0\" karaoke=\"0\" forced=\"0\" hearing_impaired=\"0\" visual_impaired=\"0\" clean_effects=\"0\" attached_pic=\"0\"/>\n" +
            "                </stream>\n" +
            "            </streams>\n" +
            "        </program>\n" +
            "        <program program_id=\"134\" program_num=\"134\" nb_streams=\"0\" pmt_pid=\"1340\" pcr_pid=\"0\">\n" +
            "            <tag key=\"service_name\" value=\"CETV\"/>\n" +
            "            <tag key=\"service_provider\" value=\"\"/>\n" +
            "            <streams>\n" +
            "            </streams>\n" +
            "        </program>\n" +
            "        <program program_id=\"130\" program_num=\"0\" nb_streams=\"0\" pmt_pid=\"0\" pcr_pid=\"0\">\n" +
            "            <tag key=\"service_name\" value=\"CCTV-7\"/>\n" +
            "            <tag key=\"service_provider\" value=\"\"/>\n" +
            "            <streams>\n" +
            "            </streams>\n" +
            "        </program>\n" +
            "        <program program_id=\"132\" program_num=\"0\" nb_streams=\"0\" pmt_pid=\"0\" pcr_pid=\"0\">\n" +
            "            <tag key=\"service_name\" value=\"MTV+2\"/>\n" +
            "            <tag key=\"service_provider\" value=\"\"/>\n" +
            "            <streams>\n" +
            "            </streams>\n" +
            "        </program>\n" +
            "    </programs>\n" +
            "</ffprobe>\n";

    public Map<String,Object> getResult(String xmlContent){
        Map<String,Object> result = new HashMap<String, Object>();
        Element root = XmlUtils.getRootFromXmlStr(xmlContent);

        if(root!=null){
            List programs = root.selectNodes("programs/program");
            if(programs!=null&&programs.size()>0){
                List<Map<String,Object>> programList = new ArrayList<Map<String, Object>>();
                for(Object o:programs){
                    Node program = (Node) o;
                    Map<String,Object> programMap = new HashMap<String, Object>();
                    Map<String,String> parameters = ServerMessager.getParameters(program,"tag","key","value");
                    programMap.put("pid",XmlUtils.getValue(program,"@program_id",null));
                    programMap.put("startTime",XmlUtils.getValue(program,"@start_time",null));
                    programMap.put("name",parameters.get("service_name"));
                    List streamNodes = program.selectNodes("streams/stream");
                    if(streamNodes!=null&&streamNodes.size()>0){
                        List<Map<String,Object>> streams = new ArrayList<Map<String, Object>>();
                        for(Object s:streamNodes){
                            Node stream = (Node) s;
                            Map<String,Object> streamMap = new HashMap<String, Object>();
                            streamMap.put("name",XmlUtils.getValue(stream,"@codec_long_name",null));
                            streamMap.put("type",XmlUtils.getValue(stream,"@codec_type",null));
                            streams.add(streamMap);
                        }
                        programMap.put("streams",streams);
                    }
                    programList.add(programMap);
                }
                result.put("programs",programList);
            }
      }
        return result;
    }
%>