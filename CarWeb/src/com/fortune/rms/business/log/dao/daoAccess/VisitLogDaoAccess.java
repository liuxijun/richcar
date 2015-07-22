package com.fortune.rms.business.log.dao.daoAccess;

import com.fortune.common.business.base.dao.BaseDaoAccess;
import com.fortune.rms.business.content.logic.logicInterface.ContentLogicInterface;
import com.fortune.rms.business.log.dao.daoInterface.VisitLogDaoInterface;
import com.fortune.rms.business.log.model.*;
import com.fortune.rms.business.publish.logic.logicInterface.ChannelLogicInterface;
import com.fortune.rms.business.publish.model.Channel;
import com.fortune.util.PageBean;
import com.fortune.util.SpringUtils;
import com.fortune.util.StringUtils;
import com.fortune.util.TreeUtils;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

@Repository
public class VisitLogDaoAccess extends BaseDaoAccess<VisitLog, Long>
        implements
        VisitLogDaoInterface {

    public VisitLogDaoAccess() {
        super(VisitLog.class);
    }

    @Autowired
    private ChannelLogicInterface channelLogicInterface;

    public void setChannelLogicInterface(ChannelLogicInterface channelLogicInterface) {
        this.channelLogicInterface = channelLogicInterface;
    }

    public static Connection getConnection()
            throws Exception {

        return DataSourceUtils.getConnection((DataSource) SpringUtils.getBean("dataSource"));
    }

    public void JDBCSaveVisitLogs(List<VisitLog> visitLogs) {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = getConnection();
            DatabaseMetaData metaData = con.getMetaData();
            boolean isOracle=metaData.getDriverName().contains("oracle");
            String sqlHeader = "insert into visit_log(";
            String sqlBody="sp_id,cp_id,channel_id,content_id,content_property_id,url,user_id,user_ip,area_id,is_free," +
                    "start_time,end_time,length,status,play_version,user_agent,avgband_width,s_ip,bytes_send";
            if(isOracle){
                sqlBody="id,"+sqlBody;
            }
            sqlHeader += sqlBody+")";
            String values = "values(";
            if(isOracle){
                values+="FORTUNE_LOG_SEQ.NEXTVAL,";
            }
            int fieldCount = sqlBody.split(",").length;
            for(int i=0;i<fieldCount;i++){
                values+="?";
                if(i<fieldCount-1){
                    values+=",";
                }
            }
            values+=")";
            String sql = sqlHeader+values;
            logger.debug("?????§Ö?sql:"+sql);
            ps = con.prepareStatement(sql);
            for (VisitLog visitLog : visitLogs) {
                ps.setLong(1, visitLog.getSpId());
                ps.setLong(2, visitLog.getCpId());
                ps.setLong(3, visitLog.getChannelId());
                ps.setLong(4, visitLog.getContentId());
                ps.setLong(5, visitLog.getContentPropertyId());
                ps.setString(6, visitLog.getUrl());
                ps.setString(7, visitLog.getUserId());
                ps.setString(8, visitLog.getUserIp());
                ps.setLong(9, visitLog.getAreaId());
                ps.setLong(10, visitLog.getIsFree());
                ps.setTimestamp(11, new Timestamp(visitLog.getStartTime().getTime()));
                ps.setTimestamp(12, new Timestamp(visitLog.getEndTime().getTime()));
                ps.setLong(13, visitLog.getLength());
                ps.setLong(14, visitLog.getStatus());
                ps.setString(15, visitLog.getPlayerVersion());
                ps.setString(16, visitLog.getUserAgent());
                ps.setLong(17, visitLog.getAvgBandwidth());
                ps.setString(18, visitLog.getsIp());
                ps.setLong(19,visitLog.getBytesSend());
                ps.addBatch();
            }
            ps.executeBatch();
        } catch (Exception e) {
            logger.error("Error when try to insert visitLog:"+e.getMessage());
            //e.printStackTrace();
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }

    public List getOrganizationContributionCount(long spId, String startTime, String endTime, String channelsAndLeafs) {
        List<Object> cpLists = new ArrayList<Object>();
        if (channelsAndLeafs != null) {
            String[] channelsAndLeafsList = channelsAndLeafs.split(",");
            String channelIds = "";
            for (String aChannelsAndLeafsList : channelsAndLeafsList) {
                String[] channelAndLeaf = aChannelsAndLeafsList.split("-");
                if (channelAndLeaf.length == 2) {
                    String channelId = channelAndLeaf[0];
                    String leaf = channelAndLeaf[1];
                    if (leaf.equals("true")) {
                        channelIds += channelId + ",";

                    } else {
                        if (!"".equals(channelIds)) {
                            channelIds = channelIds.substring(0, channelIds.length() - 1);
                            cpLists.addAll(getOrganizationContributionCountIsLeafs(spId, startTime, endTime, channelIds));
                            channelIds = "";
                        }
                        cpLists.addAll(getOrganizationContributionCountIsRoot(spId, startTime, endTime, channelId));

                    }
                }

            }
            if (!"".equals(channelIds)) {
                channelIds = channelIds.substring(0, channelIds.length() - 1);
                cpLists.addAll(getOrganizationContributionCountIsLeafs(spId, startTime, endTime, channelIds));
                channelIds = "";
            }
        }
        logger.debug("cpList:" + cpLists);

        return cpLists;
    }

    public List getSpContentTotalTime(String contentStatus, String channelsAndLeafs) {
//        if (channelsAndLeafs != null) {
//            String[] channelsAndLeafsList = channelsAndLeafs.split(",");
//            String channelIds = "";
//            for (int i = 0; i < channelsAndLeafsList.length; i++) {
//                String[] channelAndLeaf = channelsAndLeafsList[i].split("-");
//                if (channelAndLeaf.length == 2) {
//                    String channelId = channelAndLeaf[0];
//                    String leaf = channelAndLeaf[1];
//                    if (leaf.equals("true")) {
//                        channelIds += channelId + ",";
//
//                    } else {
//                        if (channelIds != "") {
//                            channelIds = channelIds.substring(0, channelIds.length() - 1);
//                            cpLists.addAll(getOrganizationContributionCountIsLeafs(spId, startTime, endTime, channelIds));
//                            channelIds = "";
//                        }
//                        cpLists.addAll(getSpContentTotalTimeIsRoot(spId, startTime, endTime, channelId));
//
//                    }
//                }
//
//            }
//            if (channelIds != "") {
//                channelIds = channelIds.substring(0, channelIds.length() - 1);
//                cpLists.addAll(getOrganizationContributionCountIsLeafs(spId, startTime, endTime, channelIds));
//                channelIds = "";
//            }
//        }
        return null;
    }

    public  List getSpContentTotalTimeIsRoot(long spId,String contentStatus,String parentChannelId){
//        TreeUtils tu = TreeUtils.getInstance();
//        List<Channel> objs = tu.getAllChildOf(Channel.class, parentChannelId, -1);
//        List cpLists = null;
//        Connection con = null;
//        PreparedStatement ps = null;
//        PreparedStatement ps1 = null;
//        PreparedStatement ps2 = null;
//        ResultSet rs = null;
//        ResultSet rs1 = null;
//        ResultSet rs2 = null;
//        String searchChannelId = parentChannelId;
//        try {
//            con = getConnection();
//            cpLists = new ArrayList();
//            for (int i = 0; i < objs.size(); i++) {
//                searchChannelId += "," + objs.get(i).getId();
//            }
//            String cpSql = "";
//            if (spId == -1) {
//                cpSql = "select sum(c.),from  channel_id in(" + searchChannelId + ") group by cp_id) cp inner join csp c on cp.cp_id = c.id ";
//
//            } else {
//                cpSql = "select cp.*,c.name as cp_name from (select cp_id,count(cp_id) as cp_num,sum((end_time-start_time)*3600*24) as cp_time from visit_log where start_time between to_date('" + startTime + "','yyyy-MM-dd') and to_date('" + endTime + "','yyyy-MM-dd') and channel_id in(" + searchChannelId + ") and sp_id =" + spId + " group by cp_id) cp inner join csp c on cp.cp_id = c.id ";
//            }
//            ps = con.prepareStatement(cpSql);
//            rs = ps.executeQuery();
//            HashMap cpMaps;
//            while (rs.next()) {
//                cpMaps = new HashMap();
//                long cpId = rs.getLong("cp_id");
//                cpMaps.put("cp_id", cpId);
//                cpMaps.put("cp_num", rs.getLong("cp_num"));
//                cpMaps.put("cp_time", rs.getDouble("cp_time"));
//                cpMaps.put("cp_name", rs.getString("cp_name"));
//                String spSql = "";
//                if (spId == -1) {
//                    spSql = "select sp.*,c.name as sp_name from (select sp_id,count(sp_id) as sp_num,sum((end_time-start_time)*3600*24) as sp_time from visit_log where start_time between to_date('" + startTime + "','yyyy-MM-dd') and to_date('" + endTime + "','yyyy-MM-dd') and channel_id in(" + searchChannelId + ")  and cp_id=" + cpId + " group by sp_id)sp inner join csp c on sp.sp_id = c.id";
//
//                } else {
//                    spSql = "select sp.*,c.name as sp_name from (select sp_id,count(sp_id) as sp_num,sum((end_time-start_time)*3600*24) as sp_time from visit_log where start_time between to_date('" + startTime + "','yyyy-MM-dd') and to_date('" + endTime + "','yyyy-MM-dd') and channel_id in(" + searchChannelId + ")  and cp_id=" + cpId + " and sp_id = " + spId + " group by sp_id)sp inner join csp c on sp.sp_id = c.id";
//                }
//                ps1 = con.prepareStatement(spSql);
//                rs1 = ps1.executeQuery();
//                List spLists = new ArrayList();
//                while (rs1.next()) {
//                    HashMap spMaps = new HashMap();
//                    long spId1 = rs1.getLong("sp_id");
//                    spMaps.put("sp_id", spId1);
//                    spMaps.put("sp_num", rs1.getLong("sp_num"));
//                    spMaps.put("sp_time", rs1.getDouble("sp_time"));
//                    spMaps.put("sp_name", rs1.getString("sp_name"));
//                    String channelSql = "";
//                    if (spId == -1) {
//                        channelSql = "select c1.*,c2.name as channel_name from(select channel_id,count(channel_id) as channel_num,sum((end_time-start_time)*3600*24) as channel_time from visit_log  where start_time between to_date('" + startTime + "','yyyy-MM-dd') and to_date('" + endTime + "','yyyy-MM-dd') and channel_id in(" + searchChannelId + ") and cp_id=" + cpId + " group by channel_id) c1 inner join channel c2 on c1.channel_id=c2.id";
//
//                    } else {
//                        channelSql = "select c1.*,c2.name as channel_name from(select channel_id,count(channel_id) as channel_num,sum((end_time-start_time)*3600*24) as channel_time from visit_log  where start_time between to_date('" + startTime + "','yyyy-MM-dd') and to_date('" + endTime + "','yyyy-MM-dd') and channel_id in(" + searchChannelId + ") and cp_id=" + cpId + " and sp_id =" + spId + " group by channel_id) c1 inner join channel c2 on c1.channel_id=c2.id";
//                    }
//                    ps2 = con.prepareStatement(channelSql);
//                    rs2 = ps2.executeQuery(channelSql);
//                    List channelLists = new ArrayList();
//                    long channelNums = 0;
//                    double channelTimes = 0;
//                    String channelNames;
//                    HashMap channelMaps = new HashMap();
//                    channelMaps.put("channel_id", parentChannelId);
//                    while (rs2.next()) {
//                        channelNums += rs2.getLong("channel_num");
//                        channelTimes += rs2.getDouble("channel_time");
//                    }
//                    channelMaps.put("channel_num", channelNums);
//                    channelMaps.put("channel_time", channelTimes);
//                    channelNames = channelLogicInterface.get(Long.valueOf(parentChannelId)).getName();
//                    channelMaps.put("channel_name", channelNames);
//                    channelLists.add(channelMaps);
//                    spMaps.put("channel_list", channelLists);
//                    spLists.add(spMaps);
//                }
//
//                cpMaps.put("sp_list", spLists);
//                cpLists.add(cpMaps);
//            }
//            logger.debug("cpList:" + cpLists);
//
//        } catch (Exception e) {
//            logger.error("???????:" + e.getMessage());
//        } finally {
//            try {
//                if (ps != null) {
//                    ps.close();
//                }
//                if (ps1 != null) {
//                    ps1.close();
//                }
//                if (ps2 != null) {
//                    ps2.close();
//                }
//                if (con != null) {
//                    con.close();
//                }
//                if (rs != null) {
//                    rs.close();
//                }
//                if (rs1 != null) {
//                    rs1.close();
//                }
//                if (rs2 != null) {
//                    rs2.close();
//                }
//
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }
        return null;
    }

    public List getOrganizationContributionCountIsRoot(long spId, String startTime, String endTime, String parentChannelId) {
        TreeUtils tu = TreeUtils.getInstance();
        List<Channel> objs = tu.getAllChildOf(Channel.class, parentChannelId, -1);
        List cpLists = null;
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        PreparedStatement ps2 = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        ResultSet rs2 = null;
        String searchChannelId = parentChannelId;
        try {
            con = getConnection();
            cpLists = new ArrayList();
            for (Channel obj : objs) {
                searchChannelId += "," + obj.getId();
            }
            String cpSql;
            if (spId == -1) {
                cpSql = "select cp.*,c.name as cp_name from (select cp_id,count(cp_id) as cp_num,sum((end_time-start_time)*3600*24) as cp_time from visit_log where start_time between to_date('" + startTime + "','yyyy-MM-dd') and to_date('" + endTime + "','yyyy-MM-dd') and channel_id in(" + searchChannelId + ") group by cp_id) cp inner join csp c on cp.cp_id = c.id ";

            } else {
                cpSql = "select cp.*,c.name as cp_name from (select cp_id,count(cp_id) as cp_num,sum((end_time-start_time)*3600*24) as cp_time from visit_log where start_time between to_date('" + startTime + "','yyyy-MM-dd') and to_date('" + endTime + "','yyyy-MM-dd') and channel_id in(" + searchChannelId + ") and sp_id =" + spId + " group by cp_id) cp inner join csp c on cp.cp_id = c.id ";
            }
            ps = con.prepareStatement(cpSql);
            rs = ps.executeQuery();
            HashMap<String,Object> cpMaps;
            while (rs.next()) {
                cpMaps = new HashMap<String,Object>();
                long cpId = rs.getLong("cp_id");
                cpMaps.put("cp_id", cpId);
                cpMaps.put("cp_num", rs.getLong("cp_num"));
                cpMaps.put("cp_time", rs.getDouble("cp_time"));
                cpMaps.put("cp_name", rs.getString("cp_name"));
                String spSql = "";
                if (spId == -1) {
                    spSql = "select sp.*,c.name as sp_name from (select sp_id,count(sp_id) as sp_num,sum((end_time-start_time)*3600*24) as sp_time from visit_log where start_time between to_date('" + startTime + "','yyyy-MM-dd') and to_date('" + endTime + "','yyyy-MM-dd') and channel_id in(" + searchChannelId + ")  and cp_id=" + cpId + " group by sp_id)sp inner join csp c on sp.sp_id = c.id";

                } else {
                    spSql = "select sp.*,c.name as sp_name from (select sp_id,count(sp_id) as sp_num,sum((end_time-start_time)*3600*24) as sp_time from visit_log where start_time between to_date('" + startTime + "','yyyy-MM-dd') and to_date('" + endTime + "','yyyy-MM-dd') and channel_id in(" + searchChannelId + ")  and cp_id=" + cpId + " and sp_id = " + spId + " group by sp_id)sp inner join csp c on sp.sp_id = c.id";
                }
                ps1 = con.prepareStatement(spSql);
                rs1 = ps1.executeQuery();
                List spLists = new ArrayList();
                while (rs1.next()) {
                    HashMap<String,Object> spMaps = new HashMap<String,Object>();
                    long spId1 = rs1.getLong("sp_id");
                    spMaps.put("sp_id", spId1);
                    spMaps.put("sp_num", rs1.getLong("sp_num"));
                    spMaps.put("sp_time", rs1.getDouble("sp_time"));
                    spMaps.put("sp_name", rs1.getString("sp_name"));
                    String channelSql = "";
                    if (spId == -1) {
                        channelSql = "select c1.*,c2.name as channel_name from(select channel_id,count(channel_id) as channel_num,sum((end_time-start_time)*3600*24) as channel_time from visit_log  where start_time between to_date('" + startTime + "','yyyy-MM-dd') and to_date('" + endTime + "','yyyy-MM-dd') and channel_id in(" + searchChannelId + ") and cp_id=" + cpId + " group by channel_id) c1 inner join channel c2 on c1.channel_id=c2.id";

                    } else {
                        channelSql = "select c1.*,c2.name as channel_name from(select channel_id,count(channel_id) as channel_num,sum((end_time-start_time)*3600*24) as channel_time from visit_log  where start_time between to_date('" + startTime + "','yyyy-MM-dd') and to_date('" + endTime + "','yyyy-MM-dd') and channel_id in(" + searchChannelId + ") and cp_id=" + cpId + " and sp_id =" + spId + " group by channel_id) c1 inner join channel c2 on c1.channel_id=c2.id";
                    }
                    ps2 = con.prepareStatement(channelSql);
                    rs2 = ps2.executeQuery(channelSql);
                    List channelLists = new ArrayList();
                    long channelNums = 0;
                    double channelTimes = 0;
                    String channelNames;
                    HashMap<String,Object> channelMaps = new HashMap<String,Object>();
                    channelMaps.put("channel_id", parentChannelId);
                    while (rs2.next()) {
                        channelNums += rs2.getLong("channel_num");
                        channelTimes += rs2.getDouble("channel_time");
                    }
                    channelMaps.put("channel_num", channelNums);
                    channelMaps.put("channel_time", channelTimes);
                    channelNames = channelLogicInterface.get(Long.valueOf(parentChannelId)).getName();
                    channelMaps.put("channel_name", channelNames);
                    channelLists.add(channelMaps);
                    spMaps.put("channel_list", channelLists);
                    spLists.add(spMaps);
                }

                cpMaps.put("sp_list", spLists);
                cpLists.add(cpMaps);
            }
            logger.debug("cpList:" + cpLists);

        } catch (Exception e) {
            logger.error("???????:" + e.getMessage());
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (ps1 != null) {
                    ps1.close();
                }
                if (ps2 != null) {
                    ps2.close();
                }
                if (con != null) {
                    con.close();
                }
                if (rs != null) {
                    rs.close();
                }
                if (rs1 != null) {
                    rs1.close();
                }
                if (rs2 != null) {
                    rs2.close();
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return cpLists;
    }

    public List getOrganizationContributionCountIsLeafs(long spId, String startTime, String endTime, String channelIds) {
        List cpLists = null;
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        PreparedStatement ps2 = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        ResultSet rs2 = null;
        try {
            con = getConnection();
            cpLists = new ArrayList();
            String cpSql = "";
            if (spId == -1) {
                cpSql = "select cp.*,c.name as cp_name from (select cp_id,count(cp_id) as cp_num,sum((end_time-start_time)*3600*24) as cp_time from visit_log where start_time between to_date('" + startTime + "','yyyy-MM-dd') and to_date('" + endTime + "','yyyy-MM-dd') and channel_id in(" + channelIds + ") group by cp_id) cp inner join csp c on cp.cp_id = c.id ";

            } else {
                cpSql = "select cp.*,c.name as cp_name from (select cp_id,count(cp_id) as cp_num,sum((end_time-start_time)*3600*24) as cp_time from visit_log where start_time between to_date('" + startTime + "','yyyy-MM-dd') and to_date('" + endTime + "','yyyy-MM-dd') and channel_id in(" + channelIds + ") and sp_id = " + spId + " group by cp_id) cp inner join csp c on cp.cp_id = c.id ";
            }
            ps = con.prepareStatement(cpSql);
            rs = ps.executeQuery();
            HashMap cpMaps;
            while (rs.next()) {
                cpMaps = new HashMap();
                long cpId = rs.getLong("cp_id");
                cpMaps.put("cp_id", cpId);
                cpMaps.put("cp_num", rs.getLong("cp_num"));
                cpMaps.put("cp_time", rs.getDouble("cp_time"));
                cpMaps.put("cp_name", rs.getString("cp_name"));
                String spSql = "";
                if (spId == -1) {
                    spSql = "select sp.*,c.name as sp_name from (select sp_id,count(sp_id) as sp_num,sum((end_time-start_time)*3600*24) as sp_time from visit_log where start_time between to_date('" + startTime + "','yyyy-MM-dd') and to_date('" + endTime + "','yyyy-MM-dd') and channel_id in(" + channelIds + ")  and cp_id=" + cpId + " group by sp_id)sp inner join csp c on sp.sp_id = c.id";

                } else {
                    spSql = "select sp.*,c.name as sp_name from (select sp_id,count(sp_id) as sp_num,sum((end_time-start_time)*3600*24) as sp_time from visit_log where start_time between to_date('" + startTime + "','yyyy-MM-dd') and to_date('" + endTime + "','yyyy-MM-dd') and channel_id in(" + channelIds + ")  and cp_id=" + cpId + " and sp_id = " + spId + " group by sp_id)sp inner join csp c on sp.sp_id = c.id";
                }
                ps1 = con.prepareStatement(spSql);
                rs1 = ps1.executeQuery();
                List spLists = new ArrayList();
                while (rs1.next()) {
                    HashMap spMaps = new HashMap();
                    long spId1 = rs1.getLong("sp_id");
                    spMaps.put("sp_id", spId1);
                    spMaps.put("sp_num", rs1.getLong("sp_num"));
                    spMaps.put("sp_time", rs1.getDouble("sp_time"));
                    spMaps.put("sp_name", rs1.getString("sp_name"));
                    String channelSql = "";
                    if (spId == -1) {
                        channelSql = "select c1.*,c2.name as channel_name from(select channel_id,count(channel_id) as channel_num,sum((end_time-start_time)*3600*24) as channel_time from visit_log  where start_time between to_date('" + startTime + "','yyyy-MM-dd') and to_date('" + endTime + "','yyyy-MM-dd') and channel_id in(" + channelIds + ") and cp_id=" + cpId + " group by channel_id) c1 inner join channel c2 on c1.channel_id=c2.id";

                    } else {
                        channelSql = "select c1.*,c2.name as channel_name from(select channel_id,count(channel_id) as channel_num,sum((end_time-start_time)*3600*24) as channel_time from visit_log  where start_time between to_date('" + startTime + "','yyyy-MM-dd') and to_date('" + endTime + "','yyyy-MM-dd') and channel_id in(" + channelIds + ") and cp_id=" + cpId + " and sp_id = " + spId + " group by channel_id) c1 inner join channel c2 on c1.channel_id=c2.id";
                    }
                    ps2 = con.prepareStatement(channelSql);
                    rs2 = ps2.executeQuery(channelSql);
                    List channelLists = new ArrayList();
                    while (rs2.next()) {
                        HashMap channelMaps = new HashMap();
                        channelMaps.put("channel_id", rs2.getLong("channel_id"));
                        channelMaps.put("channel_num", rs2.getLong("channel_num"));
                        channelMaps.put("channel_time", rs2.getDouble("channel_time"));
                        channelMaps.put("channel_name", rs2.getString("channel_name"));
                        channelLists.add(channelMaps);
                    }
                    spMaps.put("channel_list", channelLists);
                    spLists.add(spMaps);
                }

                cpMaps.put("sp_list", spLists);
                cpLists.add(cpMaps);
            }
            logger.debug("cpList:" + cpLists);

        } catch (Exception e) {
            logger.error("???????:" + e.getMessage());
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (ps1 != null) {
                    ps1.close();
                }
                if (ps2 != null) {
                    ps2.close();
                }
                if (con != null) {
                    con.close();
                }
                if (rs != null) {
                    rs.close();
                }
                if (rs1 != null) {
                    rs1.close();
                }
                if (rs2 != null) {
                    rs2.close();
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return cpLists;
    }

    public Map getResourceContributionCount(long spId, long cpId, String startTime, String endTime, String channelsAndLeafs, String contentName, String channelSelect, String playTimeSelect, PageBean pageBean) {
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        PreparedStatement ps2 = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        ResultSet rs2 = null;
        Map<String, Object> objs = new HashMap<String, Object>();
        try {
            con = getConnection();
            String cpSql = "";
            if (spId == -1) {
                cpSql = "select v1.content_name,v1.cp_id,v1.cp_name,v1.channel_id,v1.cp_num,v1.cp_time" +
                        " from (select c.name as content_name,c1.id as cp_id,c1.name as cp_name,v_l.channel_id,count(c1.id) as cp_num ,sum((end_time-start_time)*3600*24) as cp_time" +
                        " from visit_log v_l " +
                        " inner join content c on c.id = v_l.content_id" +
                        " inner join csp c1 on c1.id = v_l.cp_id" +
                        " and v_l.start_time between to_date('" + startTime + "','yyyy-MM-dd') and to_date('" + endTime + "','yyyy-MM-dd')" +
                        " group by c.name,c1.id,c1.name,v_l.channel_id  order  by c.name)v1 where 1=1 ";
            } else {
                cpSql = "select v1.content_name,v1.cp_id,v1.cp_name,v1.channel_id,v1.cp_num,v1.cp_time" +
                        " from (select c.name as content_name,c1.id as cp_id,c1.name as cp_name,v_l.channel_id,count(c1.id) as cp_num ,sum((end_time-start_time)*3600*24) as cp_time" +
                        " from visit_log v_l " +
                        " inner join content c on c.id = v_l.content_id" +
                        " inner join csp c1 on c1.id = v_l.cp_id" +
                        " and v_l.sp_id =" + spId + " and v_l.start_time between to_date('" + startTime + "','yyyy-MM-dd') and to_date('" + endTime + "','yyyy-MM-dd')" +
                        " group by c.name,c1.id,c1.name,v_l.channel_id  order  by c.name)v1 where 1=1 ";

            }
            if (playTimeSelect.equals("0")) {
                cpSql += "and v1.cp_time>10*60*60";
            }
            if (!contentName.equals("")) {
                cpSql += "and v1.content_name = '" + contentName + "'";
            }
            if (cpId != 0) {
                cpSql += "and v1.cp_id =" + cpId + "";
            }
            if (channelSelect.equals("0")) {
                if (!channelsAndLeafs.equals("")) {
                    if (channelsAndLeafs != null) {
                        String[] channelsAndLeafsList = channelsAndLeafs.split(",");
                        String channelIds = "";
                        for (int i = 0; i < channelsAndLeafsList.length; i++) {
                            String[] channelAndLeaf = channelsAndLeafsList[i].split("-");
                            if (channelAndLeaf.length == 2) {
                                String channelId = channelAndLeaf[0];
                                String leaf = channelAndLeaf[1];
                                if (leaf.equals("true")) {
                                    channelIds += channelId + ",";

                                } else {
                                    TreeUtils tu = TreeUtils.getInstance();
                                    List<Channel> channels = tu.getAllChildOf(Channel.class, channelId, -1);
                                    for (int j = 0; j < channels.size(); j++) {
                                        channelIds += channels.get(j).getId() + ",";
                                    }
                                }
                            }

                        }
                        if (channelIds != "") {
                            channelIds = channelIds.substring(0, channelIds.length() - 1);
                            cpSql += "and v1.channel_id in (" + channelIds + ")";
                        }

                    }
                }
            }
            String cpSqlCount = "select count(*) from (" + cpSql + ")";
            cpSql = "select content_name,cp_id,cp_name,cp_num,cp_time" +
                    " from (select A.*,rownum ru" +
                    " from(" + cpSql + ")A) where ru between " + pageBean.getStartRow() + " and " + pageBean.getEndRow() + "";
            ps = con.prepareStatement(cpSql);
            rs = ps.executeQuery();
            ps2 = con.prepareStatement(cpSqlCount);
            rs2 = ps2.executeQuery();
            while (rs2.next()) {
                pageBean.setRowCount(rs2.getInt(1));
            }
            List<Map> cpLists = new ArrayList<Map>();
            Map<String, Object> cpMaps;
            while (rs.next()) {
                cpMaps = new HashMap<String, Object>();
                String contentNameTemp = rs.getString("content_name");
                cpMaps.put("content_name", contentNameTemp);
                cpMaps.put("cp_name", rs.getString("cp_name"));
                cpMaps.put("cp_num", rs.getString("cp_num"));
                cpMaps.put("cp_time", formatTime(rs.getDouble("cp_time")));
                String spSql = "";
                if (spId == -1) {
                    spSql = "select v1.content_name,v1.channel_name,v1.sp_name,v1.sp_num,v1.sp_time from (select c.name as content_name,c2.name as channel_name,c1.name as sp_name,count(c1.id) as sp_num ,sum((end_time-start_time)*3600*24) as sp_time" +
                            "        from visit_log v_l" +
                            "        inner join content c on c.id = v_l.content_id" +
                            "        inner join csp c1 on c1.id = v_l.sp_id" +
                            "        inner join channel c2 on c2.id = v_l.channel_id" +
                            "        and v_l.start_time between to_date('" + startTime + "','yyyy-MM-dd') and to_date('" + endTime + "','yyyy-MM-dd')" +
                            "       and c.name = '" + contentNameTemp + "'" +
                            "        group by c.name,c1.name,c2.name  order  by c.name)v1 where 1=1";
                } else {
                    spSql = "select v1.content_name,v1.channel_name,v1.sp_name,v1.sp_num,v1.sp_time from (select c.name as content_name,c2.name as channel_name,c1.name as sp_name,count(c1.id) as sp_num ,sum((end_time-start_time)*3600*24) as sp_time" +
                            "        from visit_log v_l" +
                            "        inner join content c on c.id = v_l.content_id" +
                            "        inner join csp c1 on c1.id = v_l.sp_id" +
                            "        inner join channel c2 on c2.id = v_l.channel_id" +
                            "        and v_l.spId = v_l.start_time between to_date('" + startTime + "','yyyy-MM-dd') and to_date('" + endTime + "','yyyy-MM-dd')" +
                            "       and c.name = '" + contentNameTemp + "'" +
                            "        group by c.name,c1.name,c2.name  order  by c.name)v1 where 1=1";

                }

                if (playTimeSelect.equals("0")) {
                    spSql += "and v1.sp_time>10*60*60";
                }
                if (!contentName.equals("")) {
                    spSql += "and v1.content_name = " + contentName + "";
                }
                if (channelSelect.equals("1")) {

                }
                ps1 = con.prepareStatement(spSql);
                rs1 = ps1.executeQuery();
                List<Map> spLists = new ArrayList<Map>();
                while (rs1.next()) {
                    Map<String, Object> spMap = new HashMap<String, Object>();
                    spMap.put("content_name", rs1.getString("content_name"));
                    spMap.put("channel_name", rs1.getString("channel_name"));
                    spMap.put("sp_name", rs1.getString("sp_name"));
                    spMap.put("sp_num", rs1.getString("sp_num"));
                    spMap.put("sp_time", formatTime(rs1.getDouble("sp_time")));
                    spLists.add(spMap);
                }
                cpMaps.put("sp_list", spLists);
                cpLists.add(cpMaps);

            }
            objs.put("cp_list", cpLists);
            objs.put("cpId", cpId);
            objs.put("startTime", startTime);
            objs.put("endTime", endTime);
            objs.put("channelsAndLeafs", channelsAndLeafs);
            objs.put("contentName", contentName);
            objs.put("channelSelect", channelSelect);
            objs.put("playTimeSelect", playTimeSelect);
        } catch (Exception e) {
            logger.error("???????:" + e.getMessage());
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (ps1 != null) {
                    ps1.close();
                }

                if (ps2 != null) {
                    ps2.close();
                }

                if (rs != null) {
                    rs.close();
                }
                if (rs1 != null) {
                    rs1.close();
                }
                if (rs2 != null) {
                    rs2.close();
                }
            } catch (SQLException e) {
                logger.error("sql?????????????");
            }
        }
        return objs;
    }

    public String formatTime(double secondTime) {
        String str = "";
        long hour = 0;
        long minute = 0;
        long second = 0;
        if (secondTime != 0) {
            hour = new Double(secondTime / 3600).longValue();
            secondTime = secondTime - hour * 3600;
            minute = new Double(secondTime / 60).longValue();
            secondTime = secondTime - minute * 60;
            second = new Double(secondTime).longValue();
        }


        str += hour + "???";

        str += minute + "??";

        str += second + "??";

        return str;

    }

    public List getAreaContributionCount(long spId, String startTime, String endTime) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Map> areaList = new ArrayList<Map>();
        try {
            con = getConnection();
            String sql = "";
            if (spId == -1) {
                sql = "select area_id,a.name as area_name,count(*) as sp_num,sum((end_time-start_time)*3600*24) as sp_time from visit_log v_l" +
                        "  left join area a on v_l.area_id = a.id group by a.name,v_l.area_id order by sp_time";

            } else {
                sql = "select area_id,a.name as area_name,count(*) as sp_num,sum((end_time-start_time)*3600*24) as sp_time from visit_log v_l" +
                        "  left join area a on v_l.area_id = a.id and v_l.sp_id =" + spId + " group by a.name,v_l.area_id order by sp_time";

            }
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            Map<String, Object> areaMaps = null;
//            int i=0;
            while (rs.next()) {
                areaMaps = new HashMap();
                long sp_num = rs.getLong("sp_num");
                double sp_time = rs.getDouble("sp_time");
                areaMaps.put("area_name", rs.getString("area_name"));
                areaMaps.put("sp_num", sp_num);
                areaMaps.put("sp_time", sp_time);
//                areaMaps.put("sp_time_rank",++i);
                areaList.add(areaMaps);
            }

        } catch (Exception e) {
            logger.error("sql???????????:" + e.getMessage());
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                logger.error("??????????????" + e.getMessage());
            }
        }
        return areaList;
    }

    public Long getOnLineUserAnalysisCount(long spId, long cpId, String culTime) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Long result = null;
        try {
            con = getConnection();
            String sql = "";
            if (spId == -1) {
                sql = "select count(*) from visit_log " +
                        "where start_time <= to_date('" + culTime + "','yyyy-mm-dd hh24:mi:ss')" +
                        " and end_time > to_date('" + culTime + "','yyyy-mm-dd hh24:mi:ss')";

            } else {
                sql = "select count(*) from visit_log " +
                        "where sp_id = " + spId + " and start_time <= to_date('" + culTime + "','yyyy-mm-dd hh24:mi:ss')" +
                        " and end_time > to_date('" + culTime + "','yyyy-mm-dd hh24:mi:ss')";
            }
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                result = rs.getLong(1);
            }

        } catch (Exception e) {
            logger.error("sql???????????:" + e.getMessage());
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                logger.error("??????????????" + e.getMessage());
            }
        }
        return result;
    }


    public List getActivityUserAnalysisCount(long spId, String startTime, String endTime, PageBean pageBean) {
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement ps2 = null;
        ResultSet rs = null;
        ResultSet rs2 = null;
        List<Map> activityUserAnalysisList = new ArrayList<Map>();
        try {
            con = getConnection();
            String sql = "";
            if (spId == -1) {
                sql = "select v_l.channel_id,c.name as channel_name,v_l.content_id,c1.name as content_name,count(*) as num from visit_log v_l" +
                        " inner join channel c on v_l.channel_id = c.id  and v_l.start_time between to_date('" + startTime + "','yyyy-MM-dd') and to_date('" + endTime + "','yyyy-MM-dd')" +
                        " inner join content c1 on v_l.content_id = c1.id group by v_l.channel_id,c.name,v_l.content_id,c1.name";

            } else {
                sql = "select v_l.channel_id,c.name as channel_name,v_l.content_id,c1.name as content_name,count(*) as num from visit_log v_l" +
                        " inner join channel c on v_l.channel_id = c.id and v_l.sp_id = " + spId + " and v_l.start_time between to_date('" + startTime + "','yyyy-MM-dd') and to_date('" + endTime + "','yyyy-MM-dd')" +
                        " inner join content c1 on v_l.content_id = c1.id group by v_l.channel_id,c.name,v_l.content_id,c1.name";
            }
            String sqlCount = "select count(*) from (" + sql + ")";
            sql = "select channel_name,content_name,num" +
                    " from (select A.*,rownum ru" +
                    " from(" + sql + ")A) where ru between " + pageBean.getStartRow() + " and " + pageBean.getEndRow() + "";

            Map<String, String> activityUserAnalysisMap = null;
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                activityUserAnalysisMap = new HashMap<String, String>();
                activityUserAnalysisMap.put("channel_name", rs.getString("channel_name"));
                activityUserAnalysisMap.put("content_name", rs.getString("content_name"));
                activityUserAnalysisMap.put("num", rs.getString("num"));
                activityUserAnalysisMap.put("start_time", startTime);
                activityUserAnalysisMap.put("end_time", endTime);
                activityUserAnalysisList.add(activityUserAnalysisMap);
            }

            ps2 = con.prepareStatement(sqlCount);
            rs2 = ps2.executeQuery();
            while (rs2.next()) {
                pageBean.setRowCount(rs2.getInt(1));
            }

        } catch (Exception e) {
            logger.error("sql???????????:" + e.getMessage());
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
                if (ps2 != null) {
                    ps2.close();
                }
                if (rs2 != null) {
                    rs2.close();
                }
            } catch (SQLException e) {
                logger.error("??????????????" + e.getMessage());
            }
        }
        return activityUserAnalysisList;
    }


    public List getUserDemandCount(long spId, long cpId, String startTime, String endTime,String endTime1, String contentName, String channelName, String userIp, String userId, String playTime, PageBean pageBean) {
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement ps2 = null;
        ResultSet rs = null;
        ResultSet rs2 = null;
        List<Map> userDemandList = new ArrayList<Map>();
        String sql = "";
        if (spId == -1) {
            sql = "select v_l.cp_id,c1.name as cp_name,v_l.channel_id,c2.name as channel_name,v_l.content_id,c3.name as content_name," +
                    " (v_l.end_time-v_l.start_time)*3600*24 as play_time,v_l.start_time,v_l.end_time," +
                    " v_l.user_ip,v_l.user_id" +
                    " from visit_log v_l inner join csp c1 on v_l.cp_id = c1.id and c1.is_cp =1" +
                    " inner join channel c2 on v_l.channel_id = c2.id inner join content c3 on v_l.content_id = c3.id";

        } else {
            sql = "select v_l.cp_id,c1.name as cp_name,v_l.channel_id,c2.name as channel_name,v_l.content_id,c3.name as content_name," +
                    " (v_l.end_time-v_l.start_time)*3600*24 as play_time,v_l.start_time,v_l.end_time," +
                    " v_l.user_ip,v_l.user_id" +
                    " from visit_log v_l inner join csp c1 on v_l.cp_id = c1.id and c1.is_cp =1 and sp_id = " + spId + "" +
                    " inner join channel c2 on v_l.channel_id = c2.id inner join content c3 on v_l.content_id = c3.id";

        }
        sql = "select cp_id,cp_name,channel_name,content_name,play_time,start_time,end_time,user_ip,user_id from(" + sql + ") where 1=1";
        if (startTime != null && endTime1 != null && !startTime.equals("") && !endTime1.equals("")) {
            sql += " and start_time between to_date('" + startTime + "','yyyy-MM-dd') and to_date('" + endTime1 + "','yyyy-MM-dd')";
        }
        if (contentName != null && !contentName.equals("")) {
            sql += " and content_name like '%" + contentName.trim() + "%'";
        }
        if (channelName != null && !channelName.equals("")) {
            sql += " and channel_name like '%" + channelName.trim() + "%'";
        }
        if (userIp != null && !userIp.equals("")) {
            sql += " and user_ip = '" + userIp.trim() + "'";
        }
        if (userId != null && !userId.equals("")) {
            sql += " and user_id = '" + userId.trim() + "'";
        }
        if (cpId > 0) {
            sql += " and cp_id = '" + cpId + "'";
        }
        if (playTime != null && !playTime.equals("")) {
            sql += " and play_time >= " + Long.parseLong(playTime.trim()) * 60 + "";
        }
        String sqlCount = "select count(*) from (" + sql + ")";
        sql = "select cp_name,channel_name,content_name,play_time,start_time,end_time,user_ip,user_id" +
                " from (select A.*,rownum ru" +
                " from(" + sql + ")A) where ru between " + pageBean.getStartRow() + " and " + pageBean.getEndRow() + "";


        try {
            con = getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            Map<String, Object> userDemandMap = null;
            while (rs.next()) {
                userDemandMap = new HashMap<String, Object>();
                userDemandMap.put("cp_name", rs.getString("cp_name"));
                userDemandMap.put("channel_name", rs.getString("channel_name"));
                userDemandMap.put("content_name", rs.getString("content_name"));
                userDemandMap.put("play_time", formatTime(rs.getDouble("play_time")));
                userDemandMap.put("start_time", rs.getString("start_time"));
                userDemandMap.put("end_time", rs.getString("end_time"));
                userDemandMap.put("user_ip", rs.getString("user_ip"));
                userDemandMap.put("user_id", rs.getString("user_id"));
                userDemandList.add(userDemandMap);
            }

            ps2 = con.prepareStatement(sqlCount);
            rs2 = ps2.executeQuery();
            while (rs2.next()) {
                pageBean.setRowCount(rs2.getInt(1));
            }

        } catch (Exception e) {
            logger.error("sql???????????:" + e.getMessage());
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
                if (ps2 != null) {
                    ps2.close();
                }
                if (rs2 != null) {
                    rs2.close();
                }
            } catch (SQLException e) {
                logger.error("??????????????" + e.getMessage());
            }
        }
        return userDemandList;
    }


    public List getChannelOnDemandCount(long spId, String startTime, String endTime,Long isFree, PageBean pageBean) {
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement ps2 = null;
        ResultSet rs = null;
        ResultSet rs2 = null;
        List<Map> channelOnDemandList = new ArrayList<Map>();
        try{
            con = getConnection();
            String sql = "";
            if(spId!=-1){
                sql = "select count(distinct v1.user_id) as play_num,c1.name as cp_name,c2.name as channel_name " +
                        "from visit_log v1 inner join csp c1 on c1.id = v1.cp_id " +
                        " inner join channel c2 on c2.id = v1.channel_id and v1.sp_id="+spId+" ";
            }else{
                sql = "select count(distinct v1.user_id) as play_num,c1.name as cp_name,c2.name as channel_name " +
                        "from visit_log v1 inner join csp c1 on c1.id = v1.cp_id " +
                        " inner join channel c2 on c2.id = v1.channel_id  ";

            }
            if(isFree!=-1){
                sql += "and v1.is_free="+isFree+"";
            }
            sql += " group by c1.name,c2.name";
            String sqlCount = "select count(*) from ("+sql+")";
            sql = "select play_num,cp_name,channel_name" +
                    " from (select A.*,rownum ru" +
                    " from(" + sql + ")A) where ru between " + pageBean.getStartRow() + " and " + pageBean.getEndRow() + "";

            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            Map<String,String> channelOnDemandMap = null;
            while(rs.next()){
                channelOnDemandMap = new HashMap<String, String>();
                channelOnDemandMap.put("play_num",rs.getString("play_num"));
                channelOnDemandMap.put("cp_name",rs.getString("cp_name"));
                channelOnDemandMap.put("channel_name",rs.getString("channel_name"));
                channelOnDemandList.add(channelOnDemandMap);
            }
            ps2 = con.prepareStatement(sqlCount);
            rs2 = ps2.executeQuery();
            while(rs2.next()){
                pageBean.setRowCount(rs2.getInt(1));
            }

        }catch (Exception e) {
            logger.error("sql???????????:" + e.getMessage());
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
                if (ps2 != null) {
                    ps2.close();
                }
                if (rs2 != null) {
                    rs2.close();
                }
            } catch (SQLException e) {
                logger.error("??????????????" + e.getMessage());
            }
        }
        return channelOnDemandList;
    }

    public List getChannelDemandCount(long spId, String startTime, String endTime, String channelsAndLeafs) {
        List<Map> channelList = new ArrayList<Map>();
        Map<String,Object> channelMap = null;
        if (!channelsAndLeafs.equals("")) {
            if (channelsAndLeafs != null) {
                String[] channelsAndLeafsList = channelsAndLeafs.split(",");

                for (int i = 0; i < channelsAndLeafsList.length; i++) {
                    String sql = "select distinct v1.user_ip,v1.channel_id,c.name as channel_name from visit_log v1 inner join channel c on v1.channel_id = c.id and v1.start_time between to_date('" + startTime + "','yyyy-MM-dd') and to_date('" + endTime + "','yyyy-MM-dd') ";
                    String channelIds = "";
                    channelMap = new HashMap<String, Object>();
                    String[] channelAndLeaf = channelsAndLeafsList[i].split("-");
                    if (channelAndLeaf.length == 2) {
                        String channelId = channelAndLeaf[0];
                        String channelName = channelLogicInterface.get(Long.parseLong(channelId)).getName();
                        channelMap.put("channel_name",channelName);
                        String leaf = channelAndLeaf[1];
                        if (leaf.equals("true")) {
                            channelIds += channelId + ",";

                        } else {
                            TreeUtils tu = TreeUtils.getInstance();
                            List<Channel> channels = tu.getAllChildOf(Channel.class, channelId, -1);
                            for (int j = 0; j < channels.size(); j++) {
                                channelIds += channels.get(j).getId() + ",";
                            }
                        }
                    }

                    if (channelIds != "") {
                        channelIds = channelIds.substring(0, channelIds.length() - 1);
                        sql += "and v1.channel_id in (" + channelIds + ")";
                    }
                    channelMap.put("channel_num",getChannelDemandCount(sql));
                    channelList.add(channelMap);
                }
            }
        }

        return channelList;
    }

    public long getChannelDemandCount(String channelSql) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        long count = -1;
        try {
            channelSql = "select count(*) from (" + channelSql + ")";
            con = getConnection();
            ps = con.prepareStatement(channelSql);
            rs = ps.executeQuery();
            while (rs.next()) {
                count = rs.getLong(1);
            }

        } catch (Exception e) {
            logger.error("sql???????????:" + e.getMessage());
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                logger.error("??????????????" + e.getMessage());
            }
            return count;
        }
    }

    public List getResourceOnDemandCount(long spId, String startTime, String endTime,String channelName,String playTime,PageBean pageBean) {
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement ps2 = null;
        ResultSet rs = null;
        ResultSet rs2 = null;
        List<Map> resourceList = new ArrayList<Map>();
        try {
            String resourceSql ="";
            if(spId!=-1){
                resourceSql = "select count(distinct v1.user_id) as play_num,c1.name as content_name,c2.name as channel_name,c3.name as cp_name from visit_log v1" +
                        " inner join content c1 on c1.id = v1.content_id and v1.sp_id ="+spId+"" +
                        " inner join channel c2 on c2.id = v1.channel_id" +
                        " inner join csp c3 on c3.id = v1.cp_id" +
                        " and v1.start_time between to_date('" + startTime + "','yyyy-MM-dd') and to_date('" + endTime + "','yyyy-MM-dd')";

            }else{
                resourceSql = "select count(distinct v1.user_id) as play_num,c1.name as content_name,c2.name as channel_name,c3.name as cp_name from visit_log v1" +
                        " inner join content c1 on c1.id = v1.content_id" +
                        " inner join channel c2 on c2.id = v1.channel_id" +
                        " inner join csp c3 on c3.id = v1.cp_id" +
                        " and v1.start_time between to_date('" + startTime + "','yyyy-MM-dd') and to_date('" + endTime + "','yyyy-MM-dd')";

            }
            if(channelName !=null&&!channelName.equals("")){
                resourceSql += " and c2.name like '%"+channelName.trim()+"%'";
            }
            if(playTime!=null &&!playTime.equals("")){
                resourceSql += " and (v1.end_time-v1.start_time)*3600*24 >="+Long.parseLong(playTime.trim()) * 60+"";
            }
            resourceSql +=  " group by v1.content_id,c1.name,c2.name,c3.name";
            String resourceSqlCount = "select count(*) from ("+resourceSql+")";
            resourceSql = "select play_num,content_name,channel_name,cp_name" +
                    " from (select A.*,rownum ru" +
                    " from(" + resourceSql + ")A) where ru between " + pageBean.getStartRow() + " and " + pageBean.getEndRow() + "";

            con = getConnection();
            ps = con.prepareStatement(resourceSql);
            rs = ps.executeQuery();
            Map<String ,String> resourceMap = null;
            while(rs.next()){
                resourceMap = new HashMap<String, String>();
                resourceMap.put("play_num",rs.getString("play_num"));
                resourceMap.put("content_name",rs.getString("content_name"));
                resourceMap.put("channel_name",rs.getString("channel_name"));
                resourceMap.put("cp_name",rs.getString("cp_name"));
                resourceList.add(resourceMap);
            }

            ps2 = con.prepareStatement(resourceSqlCount);
            rs2 = ps2.executeQuery();
            while(rs2.next()){
                pageBean.setRowCount(rs2.getInt(1));
            }

        } catch (Exception e) {
            logger.error("sql???????????:" + e.getMessage());
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
                if (ps2 != null) {
                    ps2.close();
                }
                if (rs2 != null) {
                    rs2.close();
                }

            } catch (SQLException e) {
                logger.error("??????????????" + e.getMessage());
            }

        }
        return resourceList;
    }

    public int getUserActivityCount(String startTime, String endTime) {
        int total = 0;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Map<String,Long> userActivityCount = new HashMap<String,Long>();
        String sql = "select user_id,count(*) as count from visit_log where sp_id =981 group by user_id";
        if(startTime != null && endTime != null){
            sql =  "select user_id,count(*) as count from visit_log where  sp_id = 981 and start_time  between to_date('"+startTime+"','yyyy-MM-dd') and to_date('"+endTime+"','yyyy-MM-dd') group by user_id";
        }
        try {
            con = getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while(rs.next()){
                userActivityCount.put(rs.getString("user_id"),rs.getLong("count"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if (con != null) {
                    con.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }

            } catch (SQLException e) {
                logger.error("??????????????" + e.getMessage());
            }
        }

        if(userActivityCount!=null && userActivityCount.size()>0){
            long clickCount =0;
            Set set = userActivityCount.keySet();
            for(Object key : set){
                clickCount = userActivityCount.get(key);
                if(clickCount >2){
                    if(clickCount%2==0){
                        total += clickCount/2;
                    }else{
                        total += clickCount/2+1;
                    }
                }else {
                    ++total;
                }
                System.out.println("???id:"+key+"  ??????:"+userActivityCount.get(key));
            }
        }

        return total;
    }


    public static void main(String args[]) {
        VisitLogDaoAccess visitLogDaoAccess = new VisitLogDaoAccess();
//        System.out.println(visitLogDaoAccess.getUserActivityCount("2011-06-01","2012-07-30"));
        System.out.println(visitLogDaoAccess.getUserActivityCount(null,null));

    }


    public int getMaxConcurrentOfDate(String startTime, String endTime) {
        List list = new ArrayList();
        Session session = getSession();
        try {
            String hql = "select count(*) from visit_log where start_time between to_date('"+startTime+"','yyyy-mm-dd hh24:mi:ss') and to_date('"+endTime+"','yyyy-mm-dd hh24:mi:ss')" ;
            Query query = session.createSQLQuery(hql);
            list = query.list();
            return list == null || list.size() == 0?-1:Integer.parseInt(list.get(0).toString());
        } catch (HibernateException e) {
            e.printStackTrace();
            logger.debug(e.getMessage());
        }finally {
            session.close();
        }
        return  -1;
    }
    public List getAllPhoneNotIsNull(String startTime, String endTime){
        Session session = getSession();
        try {
            String sql = "select id,area_id,user_id from VISIT_LOG where  start_time >= TO_DATE('"+startTime+" 00:00:00','yyyy-mm-dd hh24:mi:ss') and end_time <=TO_DATE('"+endTime+" 23:59:59','yyyy-mm-dd hh24:mi:ss') and user_id !='null'";
            Query query = session.createSQLQuery(sql);
            List list = query.list();
            return list;
        } catch (HibernateException e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return null;
    }
    public Long getOnlineUserOfDate(Date startDate,Date endDate){
        Session session = getSession();
        String startTime = StringUtils.date2string(startDate,"yyyy-MM-dd");
        String endTime = StringUtils.date2string(endDate, "yyyy-MM-dd");
        try{
            String sql = "select count(distinct(USER_ID)) from VISIT_LOG where start_time >= to_date('"+startTime+" 00:00:00','yyyy-MM-dd hh24:mi:ss') and START_TIME <=to_date('"+endTime+" 23:59:59','yyyy-MM-dd hh24:mi:ss') and user_id != 'null'";
            Query query = session.createSQLQuery(sql);
            List list = query.list();
            return Long.parseLong(list.get(0).toString());
        } catch (HibernateException e){
            e.printStackTrace();
        } finally {
            session.close();
        }
        return null;
    }

    public List getLoginedUserInfo(String startDate,String endDate){
        Session session = getSession();
        try{
            String sql = "select distinct(USER_ID),AREA_ID from VISIT_LOG where start_time >= to_date('"+startDate+" 00:00:00','yyyy-MM-dd hh24:mi:ss') and START_TIME <=to_date('"+endDate+" 23:59:59','yyyy-MM-dd hh24:mi:ss') and user_id != 'null' order by area_id";
            Query query = session.createSQLQuery(sql);
            List list = query.list();
            return list;
        }catch (HibernateException e){
            e.printStackTrace();
        }finally {
            session.close();
        }
        return null;
    }

    public List getAreaLogsFromAreaDemandLog(Date startDate, Date endDate){
        Session session = getSession();
        String startTime = StringUtils.date2string(startDate,"yyyy-MM-dd");
        String endTime = StringUtils.date2string(endDate,"yyyy-MM-dd");
        try{
            String sql = "select id,date_Statics,area_Id,type,count,padCount,phoneCount,length,padLength,phoneLength,bytes_send,bytes_send_Pad,bytes_send_Phone,mobile_bytes_send,else_bytes_send,mobile_count,else_count,mobile_length,else_length,user_onLine_count from Area_Demand_Log where date_statics >= to_date('"+startTime+"','yyyy-MM-dd') and date_statics <=to_date('"+endTime+"','yyyy-MM-dd') order by grade asc";
            Query query = session.createSQLQuery(sql);
            List list = query.list();
            return list;
        } catch (HibernateException e){
            e.printStackTrace();
        } finally {
            session.close();
        }
        return null;
    }

    public List getAllNetFlowFromDailyStaticsLog(Date startDate,Date endDate){
        Session session = getSession();
        String startTime = StringUtils.date2string(startDate,"yyyy-MM-dd");
        String endTime = StringUtils.date2string(endDate,"yyyy-MM-dd");
        try {
//            String sql = "select date_statics,all_NetFlow,allLive_NetFlow,allContent_NetFlow,wasu_NetFlow,voole_NetFlow,bestv_NetFlow,online_User,onlineUser_NetFlow from DAILYSTATICS_LOG where date_statics like to_date('"+startDate+"','yyyy-mm-dd')";
            String sql= "select date_statics,all_NetFlow,mobile_NetFlow,else_NetFlow,all_NetFlow_Pad,all_NetFlow_Phone,allLive_NetFlow,allLive_NetFlow_Pad,allLive_NetFlow_Phone,allContent_NetFlow,allContent_NetFlow_Pad,allContent_NetFlow_Phone,wasu_NetFlow,wasuLadong_NetFlow,voole_NetFlow,vooleLadong_NetFlow,bestv_NetFlow,bestvLadong_NetFlow,online_User,onlineUser_NetFlow,all_length," +
                    "all_count,mobile_length,mobile_count,pad_length,pad_count,phone_length,phone_count,live_length,live_count,livepad_length,livepad_count,livephone_length,livephone_count,content_length,content_count,contentpad_length,contentpad_count,contentphone_length,contentphone_count,bestv_length," +
                    "bestv_count,wasu_length,wasu_count,voole_count,voole_length,else_length,else_count from DAILYSTATICS_LOG where date_statics >= TO_DATE('"+startTime+"','yyyy-mm-dd') and date_statics <= to_date('"+endTime+"','yyyy-mm-dd') order by date_statics asc";
            Query query = session.createSQLQuery(sql);
            List list= query.list();
            return list;
        } catch (HibernateException e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return null;
    }

    public List getAllBingFaFromDailyStaticsLog(Date startDate,Date endDate){
        Session session = getSession();
        String startTime = StringUtils.date2string(startDate,"yyyy-MM-dd");
        String endTime = StringUtils.date2string(endDate,"yyyy-MM-dd");
        try {
            String sql = "select QUARTER_BINGFA,max_bingfa from DAILYSTATICS_LOG where date_statics >= TO_DATE('"+startTime+"','yyyy-mm-dd') and date_statics <= to_date('"+endTime+"','yyyy-mm-dd') order by date_statics asc";
            Query query = session.createSQLQuery(sql);
            List list = query.list();
            return list;
        } catch (HibernateException e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return null;
    }

    public List getUserLoginLogs(Date startDate,Date endDate){
        Session session = getSession();
        String  startTime = StringUtils.date2string(startDate,"yyyy-MM-dd");
        String endTime = StringUtils.date2string(endDate,"yyyy-MM-dd");
        try{
            String sql = "select count(distinct(login)),area_Id,login_status from Usr_USER_Login where Login_Time >= TO_DATE('"+startTime+" 00:00:00','yyyy-mm-dd hh24:mi:ss') and login_Time <= to_date('"+endTime+" 23:59:59','yyyy-mm-dd hh24:mi:ss') group by area_Id,login_status";
            Query query = session.createSQLQuery(sql);
            List list = query.list();
            return list;
        }catch (Exception e){
            e.printStackTrace();
        }finally{
            session.close();
        }
        return null;
    }
    public List getUserCountLogs(Date startDate, Date endDate){
        Session session = getSession();
        String  startTime = StringUtils.date2string(startDate,"yyyy-MM-dd");
        String endTime = StringUtils.date2string(endDate,"yyyy-MM-dd");
        try{
            String sql = "select area_Id,count(distinct(login)) from Usr_USER_Login where Login_Time >= TO_DATE('"+startTime+" 00:00:00','yyyy-mm-dd hh24:mi:ss') and login_Time <= to_date('"+endTime+" 23:59:59','yyyy-mm-dd hh24:mi:ss') group by area_Id";
            Query query = session.createSQLQuery(sql);
            List list = query.list();
            return list;
        }catch (Exception e){
            e.printStackTrace();
        }finally{
            session.close();
        }
        return null;
    }
    public Map getUserLoginCount(String startTime, String endTime, String userIp, String userId, PageBean pageBean){
        Session session = getSession();
        Map<String,List<Object[]>> userLoginMap = new HashMap();
        String sql = "",sql1 = "";
        //select count(login),login,addr,login_status,area_Id from Usr_User_Login where login=15631127974 group by addr,login_status,area_id,login
        sql = "select count(login),login,addr,login_status,area_Id from Usr_User_Login where 1=1";
        sql1 = "select distinct(login),TO_CHAR(login_time,'yyyy-MM-dd'),area_id from USR_USER_LOGIN where 1=1";
        if (startTime != null && endTime != null && !startTime.equals("") && !endTime.equals("")) {
            sql += " and login_Time >= to_date('"+startTime+" ','yyyy-mm-dd hh24:mi:ss') and login_Time <= to_date('"+endTime+" ','yyyy-mm-dd hh24:mi:ss')";
            sql1 += " and login_Time >= to_date('"+startTime+" ','yyyy-mm-dd hh24:mi:ss') and login_Time <= to_date('"+endTime+" ','yyyy-mm-dd hh24:mi:ss')";
        }
        if (userIp != null && !userIp.equals("")) {
            sql += " and addr = '" + userIp.trim() + "'";
            sql1 += " and addr = '"+ userIp.trim()+ "'";
        }
        if (userId != null && !userId.equals("")) {
            sql += " and login = '" + userId.trim() + "'";
            sql1 += " and login = '" + userId.trim() + "'";
        }
        sql += " group by addr,login_status,area_id,login ";
        String sqlDayCount = "select count(*) from ("+sql1+")";
        Query query = session.createSQLQuery(sql);
        Query query1 = session.createSQLQuery(sqlDayCount);
        List<Object[]> list = query.list();
        List<Object[]> list1 = query1.list();
        userLoginMap.put("userLoginInfo",list);
        userLoginMap.put("userLoginDayInfo",list1);
        return userLoginMap;
    }

    public List getUserLoginCountLogs(Date startDate,Date endDate){
        Session session = getSession();
        String  startTime = StringUtils.date2string(startDate,"yyyy-MM-dd");
        String endTime = StringUtils.date2string(endDate,"yyyy-MM-dd");
        try{
            String sql = "select area_Id,count(*) from Usr_USER_Login where Login_Time >= TO_DATE('"+startTime+" 00:00:00','yyyy-mm-dd hh24:mi:ss') and login_Time <= to_date('"+endTime+" 23:59:59','yyyy-mm-dd hh24:mi:ss') group by area_Id";
            Query query = session.createSQLQuery(sql);
            List list = query.list();
            return list;
        }catch (Exception e){
            e.printStackTrace();
        }finally{
            session.close();
        }
        return null;
    }

    public List getUserDayCountLogs(Date startDate,Date endDate){
        Session session = getSession();
        String startTime = StringUtils.date2string(startDate,"yyyy-MM-dd");
        String endTime = StringUtils.date2string(endDate,"yyyy-MM-dd");
        try{
            String sql = "select distinct(login),TO_CHAR(login_time,'yyyy-MM-dd'),area_id from USR_USER_LOGIN where Login_Time >= TO_DATE('"+startTime+" 00:00:00','yyyy-mm-dd hh24:mi:ss') and login_Time <= to_date('"+endTime+" 23:59:59','yyyy-mm-dd hh24:mi:ss')";
            Query query = session.createSQLQuery(sql);
            List list = query.list();
            return list;
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            session.close();
        }
        return null;
    }

    public List getSpLogsFromVisitLog(Date startDate, Date endDate){
        Session session = getSession();
        String startTime = StringUtils.date2string(startDate,"yyyy-MM-dd");
        String endTime = StringUtils.date2string(endDate,"yyyy-MM-dd");
        Query query;
        String sql = "";
        String sqlPad = "";
        String sqlPhone = "";
        try{
            sql = "select vl.sp_Id,count(*),sum(vl.length),sum(vl.bytes_Send) from Visit_Log vl where vl.sp_Id in (select id  from CSP c where c.status = 1 ) and  vl.start_Time>= TO_DATE('"+startTime+" 00:00:00','yyyy-mm-dd hh24:mi:ss')  and  vl.start_Time<=to_date('"+endTime+" 23:59:59','yyyy-mm-dd hh24:mi:ss')  group by vl.sp_Id  order by count(*) DESC";
            sqlPad = "select vl.sp_Id,count(*),sum(vl.length),sum(vl.bytes_Send) from Visit_Log vl where vl.sp_Id in (select id  from CSP c where c.status = 1 ) and  vl.start_Time>= TO_DATE('"+startTime+" 00:00:00','yyyy-mm-dd hh24:mi:ss')  and  vl.start_Time<=to_date('"+endTime+" 23:59:59','yyyy-mm-dd hh24:mi:ss')  and (user_agent  like '%Pad%' or user_agent  like '%pad%')  group by vl.sp_Id  order by count(*) DESC";
            sqlPhone = "select vl.sp_Id,count(*),sum(vl.length),sum(vl.bytes_Send) from Visit_Log vl where vl.sp_Id in (select id  from CSP c where c.status = 1 /*select id from Csp*/) and  vl.start_Time>= TO_DATE('"+startTime+" 00:00:00','yyyy-mm-dd hh24:mi:ss')  and  vl.start_Time<=to_date('"+endTime+" 23:59:59','yyyy-mm-dd hh24:mi:ss')  and ((user_agent  not like '%Pad%' and user_agent not like '%pad%') or user_agent is null) group by vl.sp_Id  order by count(*) DESC";
            List spLog = new ArrayList();
            query = session.createSQLQuery(sql);
            List<Object[]> list = query.list();
            query = session.createSQLQuery(sqlPad);
            List<Object[]> listPad = query.list();
            query = session.createSQLQuery(sqlPhone);
            List<Object[]> listPhone = query.list();
            if(list.size()>0){
                for(Object[] o:list){
                    long spId = Long.parseLong(o[0].toString());
                    Object[] o1 = new Object[10];
                    o1[0] = spId;
                    o1[1] = o[1];
                    o1[4] = o[2];
                    o1[7] = o[3];
                    if(listPad.size()>0){
                        for(Object[] oPad:listPad){
                            if(Long.parseLong(oPad[0].toString())==spId){
                                o1[2] = oPad[1];
                                o1[5] = oPad[2];
                                o1[8] = oPad[3];
                                break;
                            }else{
                                o1[2] = 0;
                                o1[5] = 0;
                                o1[8] = 0;
                            }
                        }
                    }else{
                        o1[2] = 0;
                        o1[5] = 0;
                        o1[8] = 0;
                    }
                    if(listPhone.size()>0){
                        for(Object[] oPhone:listPhone){
                            if(Long.parseLong(oPhone[0].toString())==spId){
                                o1[3] = oPhone[1];
                                o1[6] = oPhone[2];
                                o1[9] = oPhone[3];
                                break;
                            }else{
                                o1[3] = 0;
                                o1[6] = 0;
                                o1[9] = 0;
                            }
                        }
                    }else{
                        o1[3] = 0;
                        o1[6] = 0;
                        o1[9] = 0;
                    }
                    spLog.add(o1);
                }
            }
            return spLog;
        }catch (Exception e){
            e.printStackTrace();
        }finally{
            session.close();
        }
        return null;
    }

    public List getContentByDateAndType(Date startDate, Date endDate, String type){
        Session session = getSession();
        String startTime = StringUtils.date2string(startDate,"yyyy-MM-dd");
        String endTime = StringUtils.date2string(endDate,"yyyy-MM-dd");
        try {
            String sql = "";
            Query query;

            if("systemPlayedLog".equals(type)){
                sql = "select * from (select sp_Id,content_Id,sum(count),sum(padCount),sum(phoneCount),sum(length),sum(padLength),sum(phoneLength),sum(bytes_Send),sum(bytes_Send_Pad),sum(bytes_Send_Phone) from CONTENT_DEMAND_LOG where type = 1 and  date_statics >= to_date('"+startTime+"','yyyy-MM-dd') and date_statics <=to_date('"+endTime+"','yyyy-MM-dd') group by content_Id,sp_Id order by sum(count) desc)";
            }
            if("contentLog".equals(type)){
                sql = "select * from (select sp_Id,content_Id,sum(count),sum(padCount),sum(phoneCount),sum(length),sum(padLength),sum(phoneLength),sum(bytes_Send),sum(bytes_Send_Pad),sum(bytes_Send_Phone) from CONTENT_DEMAND_LOG where type = 1 and  date_statics >= to_date('"+startTime+"','yyyy-MM-dd') and date_statics <=to_date('"+endTime+"','yyyy-MM-dd') group by content_Id,sp_Id order by sum(count) desc) where rownum < 101";
            }
            if("contentLiveLog".equals(type)){
                sql = "select sp_Id,content_Id,sum(count),sum(padCount),sum(phoneCount),sum(length),sum(padLength),sum(phoneLength),sum(bytes_Send),sum(bytes_Send_Pad),sum(bytes_Send_Phone) from CONTENT_DEMAND_LOG where type = 2 and  date_statics >= to_date('"+startTime+"','yyyy-MM-dd') and date_statics <=to_date('"+endTime+"','yyyy-MM-dd') group by content_Id,sp_Id order by sum(count) desc";
            }
            if("contentLadongLog".equals(type)){
                sql = "select * from (select sp_Id,content_Id,sum(count),sum(padCount),sum(phoneCount),sum(length),sum(padLength),sum(phoneLength),sum(bytes_Send),sum(bytes_Send_Pad),sum(bytes_Send_Phone) from CONTENT_DEMAND_LOG where type = 3 and  date_statics >= to_date('"+startTime+"','yyyy-MM-dd') and date_statics <=to_date('"+endTime+"','yyyy-MM-dd') group by content_Id,sp_Id order by sum(count) desc) where rownum < 101";
            }
            if("contentByLengthLog".equals(type)){
                sql = "select * from (select sp_Id,content_Id,sum(length),sum(padLength),sum(phoneLength),sum(count),sum(padCount),sum(phoneCount),sum(bytes_Send),sum(bytes_Send_Pad),sum(bytes_Send_Phone) from CONTENT_DEMAND_LOG where type = 1 and  date_statics >= to_date('"+startTime+"','yyyy-MM-dd') and date_statics <=to_date('"+endTime+"','yyyy-MM-dd') group by content_Id,sp_Id order by sum(length) desc) where rownum < 101";
            }
            if("contentByNetFlowLog".equals(type)){
                sql = "select * from (select sp_Id,content_Id,sum(bytes_Send),sum(bytes_Send_Pad),sum(bytes_Send_Phone),sum(length),sum(padLength),sum(phoneLength),sum(count),sum(padCount),sum(phoneCount) from CONTENT_DEMAND_LOG where type = 1 and  date_statics >= to_date('"+startTime+"','yyyy-MM-dd') and date_statics <=to_date('"+endTime+"','yyyy-MM-dd') group by content_Id,sp_Id order by sum(bytes_Send) desc) where rownum < 101";
            }
            query = session.createSQLQuery(sql);
            List<Object[]> list = query.list();
            return list;
        } catch (HibernateException e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return null;
    }

    public List getChannelLogsFromChannelDemandLog(Date startDate, Date endDate, String channelIds){
        Session session = getSession();
        String startTime = StringUtils.date2string(startDate,"yyyy-MM-dd");
        String endTime = StringUtils.date2string(endDate,"yyyy-MM-dd");
        try{
            String sql = "select channel_Id,sum(count),sum(length),sum(bytes_Send) from channel_Demand_Log where date_statics >= to_date('"+startTime+"','yyyy-MM-dd') and date_statics <=to_date('"+endTime+"','yyyy-MM-dd') ";
            if(!"".equals(channelIds)&&channelIds!=null){
                sql += " and channel_Id in ("+channelIds+") ";
            }
            sql += " group by channel_Id order by  sum(count) DESC";
            Query query = session.createSQLQuery(sql);
            List list = query.list();
            return list;
        }catch (HibernateException e){
            e.printStackTrace();
        }finally {
            session.close();
        }
        return null;
    }

    public List checkChannelFilmOrTv(long channelId){
        Session session = getSession();
        try{
            String sql = "select content_Id,count(*) from CONTENT_PROPERTY where content_id in (select content_id from CONTENT_CHANNEL  where channel_id ="+channelId+") and PROPERTY_ID = 15884973 group by content_id";
            Query query = session.createSQLQuery(sql);
            List list = query.list();
            return list;
        }catch (Exception e){
            e.printStackTrace();
        }finally{
            session.close();
        }
        return null;
    }
    public List getContentFilmAndTvLog(Date startDate,Date endDate,long contentType,String channelIds,String contentIds,int pageSize){
        Session session = getSession();
/*
        String startTime = StringUtils.date2string(startDate,"yyyy-MM-dd");
        String endTime = StringUtils.date2string(endDate,"yyyy-MM-dd");
*/
        try{
            //String sqlTable = "";//"select * from (";
            String sql = "select sp_Id,"+contentType+",content_id,channel_Id,sum(count),sum(length),sum(bytes_send)" +
                    "  from CONTENT_DEMAND_LOG where date_statics >=? and date_statics <= ? ";
            if(!"".equals(channelIds)&&channelIds!=null){
                sql += " and (content_Id in (select content_id from CONTENT_CHANNEL where channel_id in " +
                        "        ("+channelIds+")) ";
            }
            if(!"".equals(contentIds)&&contentIds!=null){
                sql += " or content_Id in ("+contentIds+")";
            } else{
                sql+=" " ;
            }
            sql += ") group by content_Id,sp_Id,channel_Id order by  sum(count) DESC";
            //sqlTable += sql +") where rownum<=" +pageSize;
            Query query = session.createSQLQuery(sql);
            query.setFirstResult(0);
            query.setDate(0,startDate);
            query.setDate(1,endDate);
            query.setMaxResults(pageSize);
            return query.list();
        }catch (Exception e){
            e.printStackTrace();
        }finally{
            session.close();
        }
        return null;
    }


    public List getTotalHotRanked() {
        Session session = getSession();
        try {
            List list;
            String sql = "select cl.CONTENT_ID,sum(cl.COUNT),(select c.NAME from CONTENT c where c.id = cl.CONTENT_ID)  " +
                    "from CONTENT_DEMAND_LOG cl where cl.TYPE = 1 and cl.date_statics > to_date('" +
                    StringUtils.date2string(new Date(System.currentTimeMillis()-24*3600*1000*3),"yyyy-MM-dd")+//?????
                    "','yyyy-mm-dd')" +  //type=1??????
                    "group by cl.CONTENT_ID order by sum(cl.COUNT) desc";

            Query query =  session.createSQLQuery(sql);
            query.setFirstResult(0);
            query.setMaxResults(10);
            return query.list();

            /*   List list = new ArrayList();
           String sql = "select cl.content_id,sum(cl.count),(select c.name from content c where c.id = cl.content_id)  " +
                   "from content_demand_log cl where cl.type = 1 " +  //type=1??????
                   "and rownum <= 10 group by cl.content_id   order by sum(cl.count) desc";

           Query query =  session.createSQLQuery(sql);
           list = query.list();
           return list;*/
        }  catch (HibernateException e) {
            e.printStackTrace();
        } finally {
            session.close();
        }

        return null;
    }


    public List getFileOrTvHotRanked(long contentType,String channelIds,String contentIds) {
        Session session = getSession();
        try {
            String sql = "select cl.CONTENT_ID,sum(cl.COUNT) CL_COUNT,(select c.NAME from CONTENT c where c.ID = cl.CONTENT_ID)  " +
                    "from CONTENT_DEMAND_LOG cl where (cl.CONTENT_ID in (select cc.CONTENT_ID from CONTENT_CHANNEL cc where cc.CHANNEL_ID in ("+channelIds+"))" +
                    "";
            if(contentIds!=null&&!"".equals(contentIds)){
                sql+=" or cl.CONTENT_ID in ("+contentIds+")";
            }
            sql+=") and cl.date_statics > to_date('" +
                    StringUtils.date2string(new Date(System.currentTimeMillis()-24*3600*1000*3),"yyyy-MM-dd")+//?????
                    "','yyyy-mm-dd') group by cl.CONTENT_ID order by  CL_COUNT DESC";
            Query query =  session.createSQLQuery(sql);
            query.setFirstResult(0);
            query.setMaxResults(10);
            /*  String sql = "select cl.content_id,sum(cl.count),(select c.name from content c where c.id = cl.content_id)  " +
                "from content_demand_log cl where (cl.content_Id in (select cc.content_id from CONTENT_CHANNEL cc where cc.channel_id in ("+channelIds+"))";

        if(contentIds!=null&&!"".equals(contentIds)){
            sql+=" or cl.CONTENT_ID in ("+contentIds+")";
        }

        sql += ") and rownum <= 10 group by cl.content_Id order by  sum(count) DESC";

        Query query =  session.createSQLQuery(sql);*/

            return query.list();
        }  catch (HibernateException e) {
            e.printStackTrace();
        } finally {
            session.close();
        }

        return null;
    }

    public List<ContentDemandLog> getContentDemandLogByDate(String startDate,String endDate) {
        String sql = "from  ContentDemandLog  where dateStatics >= to_date('"+startDate+"','yyyy-mm-dd') and dateStatics < to_date('"+endDate+"','yyyy-mm-dd')";

        return this.getHibernateTemplate().find(sql);
    }


    public List<DailyStaticsLog> getDailyStaticsLogByDate(String startDate,String endDate) {
        String sql = "from  DailyStaticsLog  where dateStatics >= to_date('"+startDate+"','yyyy-mm-dd') and dateStatics < to_date('"+endDate+"','yyyy-mm-dd')";

        return this.getHibernateTemplate().find(sql);
    }

    public List<AreaDemandLog> getAreaDemandLogByDate(String startDate,String endDate) {
        String sql = "from  AreaDemandLog  where dateStatics >= to_date('"+startDate+"','yyyy-mm-dd') and dateStatics < to_date('"+endDate+"','yyyy-mm-dd')";

        return this.getHibernateTemplate().find(sql);
    }

    public List<ChannelDemandLog> getChannelDemandLogByDate(String startDate,String endDate) {
        String sql = "from  ChannelDemandLog  where dateStatics >= to_date('"+startDate+"','yyyy-mm-dd') and dateStatics < to_date('"+endDate+"','yyyy-mm-dd')";

        return this.getHibernateTemplate().find(sql);
    }


    public List<VisitLog> getVisitLogByDate(String startDate,String endDate) {
        String sql = "from  VisitLog  where startTime >= to_date('"+startDate+"','yyyy-mm-dd') and startTime < to_date('"+endDate+"','yyyy-mm-dd')";

        return this.getHibernateTemplate().find(sql);
    }


    public List getUserLoginByDate(String startDate,String endDate) {
        String sql = "select * from  usr_User_Login ul  where ul.login_Time >= to_date('"+startDate+"','yyyy-mm-dd') and ul.login_Time < to_date('"+endDate+"','yyyy-mm-dd')";
        Session session =  getSession();
        try {

            Query query = session.createSQLQuery(sql);
            return query.list();
        } catch (DataAccessResourceFailureException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }  finally {
            session.close();
        }
        return null;
    }

    /**
     * visit log???????
     * @param channelIds ????????¦¶
     * @param startTime  ????????
     * @param endTime    ????????
     * @param pageBean   ??????
     * @return  content_id????????
     */
    @SuppressWarnings("Unchecked")
    public List<Object[]> getRedexStatVisitCount(String channelIds, Date startTime, Date endTime, PageBean pageBean){

        String hql = "select count( distinct v.contentId) from VisitLog v";
        String condition = " where v.contentId in (select c.id from Content c where c.status<>" +
                ContentLogicInterface.STATUS_DELETE + " and c.status<>" + ContentLogicInterface.STATUS_RECYCLE + ")";
        if( channelIds != null && channelIds.length() > 0){
            condition += " and v.contentId in ( select cc.contentId from ContentChannel cc " +
                    "where cc.channelId in (" + channelIds + "))";
        }
        if( startTime != null ){
            condition += " and (v.startTime >= :StartTime or v.endTime >= :StartTime)";
        }
        if( endTime != null ){
            condition += " and (v.startTime <= :EndTime or v.endTime <= :EndTime)";
        }

        Session session = getSession();
        Query q = session.createQuery(hql + condition);
        if( startTime != null ) {
            //q.setDate("StartTime", startTime);
            q.setTimestamp("StartTime", startTime);
        }
        if( endTime != null ) {
            q.setTimestamp("EndTime", endTime);
        }
        List<Object> countList = q.list();
        if( countList == null || countList.size() == 0 ){
            if( session != null && session.isOpen() ){
                session.close();
            }
            return null;
        }

        Integer count = Integer.parseInt(countList.get(0).toString());
        pageBean.setRowCount(count);

        //noinspection JpaQlInspection
        hql = "select contentId, count(*) as visitCount from VisitLog v" + condition +
                " group by contentId order by visitCount desc";
        Query recordQuery = session.createQuery(hql);
        if( startTime != null ) {
            recordQuery.setTimestamp("StartTime", startTime);
        }
        if( endTime != null ) {
            recordQuery.setTimestamp("EndTime", endTime);
        }
        recordQuery.setFirstResult(pageBean.getStartRow());
        recordQuery.setMaxResults(pageBean.getPageSize());
        List l = recordQuery.list();

        if( session != null && session.isOpen() ){
            session.close();
        }

        return l;
    }

    /**
     * top n
     * @param channelIds channel can view
     * @param userType   userType
     * @param startTime
     * @param count
     * @return
     */
    public List<Object[]> getRedexTop(String channelIds,Long userType, Date startTime, int count){
        String condition = " where v.contentId in (select c.id from Content c where c.status=" +
                ContentLogicInterface.STATUS_CP_ONLINE ;
        if(userType > 0){
            condition += " and c.userTypes like '%," + userType + ",%')";
        }else{
            condition += ")";
        }
        if( channelIds != null && channelIds.length() > 0){
            condition += " and v.contentId in ( select cc.contentId from ContentChannel cc " +
                    "where cc.channelId in (" + channelIds + "))";
        }
        if( startTime != null ){
            condition += " and (v.startTime >= :StartTime or v.endTime >= :StartTime)";
        }

        Session session = getSession();

        PageBean pageBean = new PageBean();
        pageBean.setPageNo(1);
        pageBean.setPageSize(count);
        //noinspection JpaQlInspection
        String hql = "select contentId, count(*) as visitCount from VisitLog v" + condition +
                " group by contentId order by visitCount desc";
        Query recordQuery = session.createQuery(hql);
        if( startTime != null ) {
            recordQuery.setTimestamp("StartTime", startTime);
        }
        recordQuery.setFirstResult(pageBean.getStartRow());
        recordQuery.setMaxResults(pageBean.getPageSize());
        List l = recordQuery.list();

        if( session != null && session.isOpen() ){
            session.close();
        }

        return l;
    }

    /**
     * ?????????????
     * @param startTime ????????
     * @param endTime   ????????
     * @return ???????????
     */
    public List<Object[]> getRedexChannelVisitCount(Date startTime, Date endTime){
        Session session = getSession();
        String hql = "select channelId, count(*) as visitCount from VisitLog v " +
                "where (v.startTime >= :StartTime or v.endTime >= :StartTime) " +
                "and (v.startTime <= :EndTime or v.endTime <= :EndTime) group by channelId";
        if( startTime == null ){
            startTime = new Date();
            startTime.setTime(0);   // ??1970???
        }
        if( endTime == null ){
            endTime = new Date();
            endTime.setTime(new Date().getTime() + 1000*3600*24);   // ?????????
        }
        Query recordQuery = session.createQuery(hql);
        recordQuery.setTimestamp("StartTime", startTime);
        recordQuery.setTimestamp("EndTime", endTime);

        List l = recordQuery.list();

        if( session != null && session.isOpen() ){
            session.close();
        }

        return l;
    }


    /**
     * ?????????????
     * @param timeSpot      ?????????????
     * @param startTime     ????????
     * @param endTime       ????????
     * @return  ????????
     */
    public int getTimeSpotAverageConcurrent(Date timeSpot, Date startTime, Date endTime) throws java.text.ParseException{
        Session session = getSession();

        String hql;
        // ????????????????????????????
        if( startTime == null){
            hql = "select min(v.startTime) from VisitLog v";
            Query timeQuery = session.createQuery(hql);
            List<Object> l = timeQuery.list();
            if(l == null || l.size() == 0){
                if( session != null && session.isOpen()){
                    session.close();
                }
                return 0;
            }

            startTime = StringUtils.string2date(l.get(0).toString());
        }
        if( endTime == null){
            endTime = new Date();
        }

        long day = (endTime.getTime()-startTime.getTime())/(24*60*60*1000);
        SimpleDateFormat parser = new SimpleDateFormat("HH:mm:ss");
        Date simpleStartTime = parser.parse(parser.format(startTime));
        Date simpleEndTime = parser.parse(parser.format(endTime));
        Date simpleTimeSpot = parser.parse(parser.format(timeSpot));
        if( simpleTimeSpot.after(simpleStartTime) && simpleTimeSpot.before(simpleEndTime)) day++;
        if( day == 0){
            if( session != null && session.isOpen()){
                session.close();
            }
            return 0;
        }


        hql = "select count(*) from VisitLog  v where (:TimeSpot between cast(v.startTime as time) " +
                "and cast(v.endTime as time)) " +
                "and (v.startTime >= :StartTime or v.endTime >= :StartTime) " +
                "and (v.startTime <= :EndTime or v.endTime <= :EndTime)";
        //( CAST (F.obDepartAirport AS time) between time :obDepartTimeStart and time :obDepartTimeEnd)
        Query recordQuery = session.createQuery(hql);
        recordQuery.setTime("TimeSpot", timeSpot);
        recordQuery.setTimestamp("StartTime", startTime);
        recordQuery.setTimestamp("EndTime", endTime);

        List l = recordQuery.list();

        if( session != null && session.isOpen() ){
            session.close();
        }


        return (l == null || l.size() == 0)? 0 : Integer.parseInt(l.get(0).toString())/(int)day;
    }

    public int getTimeSpotPeakConcurrent(Date timeSpot, Date startTime, Date endTime) throws java.text.ParseException{
        Session session = getSession();

        String hql;
        if( startTime == null){
            hql = "select min(v.startTime) from VisitLog v";
            Query timeQuery = session.createQuery(hql);
            List<Object> l = timeQuery.list();
            if(l == null || l.size() == 0){
                if( session != null && session.isOpen()){
                    session.close();
                }
                return 0;
            }

            startTime = StringUtils.string2date(l.get(0).toString());
        }
        if( endTime == null){
            endTime = new Date();
        }

        SimpleDateFormat parser = new SimpleDateFormat("HH:mm:ss");
        Date simpleStartTime = parser.parse(parser.format(startTime));
        Date simpleEndTime = parser.parse(parser.format(endTime));
        Date simpleTimeSpot = parser.parse(parser.format(timeSpot));
        hql = "select count(*) as visitCount from VisitLog  v where (:TimeSpot between cast(v.startTime as time) " +
                "and cast(v.endTime as time)) " +
                "and (v.startTime >= :StartTime or v.endTime >= :StartTime) " +
                "and (v.startTime <= :EndTime or v.endTime <= :EndTime) group by cast(v.startTime as date)";
        Query recordQuery = session.createQuery(hql);
        recordQuery.setTime("TimeSpot", timeSpot);
        recordQuery.setTimestamp("StartTime", startTime);
        recordQuery.setTimestamp("EndTime", endTime);

        List l = recordQuery.list();

        if( session != null && session.isOpen() ){
            session.close();
        }

        int count = 0;
        if( l != null) {
            for (Object obj : l) {
                if (Integer.parseInt(obj.toString()) > count) {
                    count = Integer.parseInt(obj.toString());
                }
            }
        }

        return count;
    }

    public List getDemandCountLogs(Date startDate,Date endDate){
        Session session = getSession();
        String startTime = StringUtils.date2string(startDate,"yyyy-MM-dd");
        String endTime = StringUtils.date2string(endDate,"yyyy-MM-dd");
        try {
            String sql = "select  count(*),to_char(vl.start_time,'yyyy-MM-dd hh24') from visit_log vl where start_Time >=to_date('"+startTime+" 00:00:00','yyyy-MM-dd hh24:mi:ss') and start_Time <to_date('"+endTime+" 23:59:59','yyyy-MM-dd hh24:mi:ss') group by to_char(vl.start_time,'yyyy-MM-dd hh24') order by to_char(vl.start_time,'yyyy-MM-dd hh24')";
            Query query = session.createSQLQuery(sql);
            List list= query.list();
            return list;
        } catch (HibernateException e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return null;
    }
    public List getSearchHotLogs(Date startDate,Date endDate){
        Session session = getSession();
        String startTime = StringUtils.date2string(startDate,"yyyy-MM-dd");
        String endTime = StringUtils.date2string(endDate,"yyyy-MM-dd");
        try {
            String sql ="select * from (select content ,search_month_count from USER_HOT_SEARCH order by search_month_count desc )where rownum < 51";
            Query query = session.createSQLQuery(sql);
            List list= query.list();
            return list;
        } catch (HibernateException e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return null;
    }

}

