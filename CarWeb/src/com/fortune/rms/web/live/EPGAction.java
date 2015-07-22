package com.fortune.rms.web.live;

import com.fortune.common.Constants;
import com.fortune.common.business.security.logic.logicInterface.AdminLogicInterface;
import com.fortune.common.business.security.model.Admin;
import com.fortune.common.web.base.BaseAction;
import com.fortune.rms.business.live.logic.logicInterface.EPGLogicInterface;
import com.fortune.rms.business.live.logic.logicInterface.LiveLogicInterface;
import com.fortune.rms.business.live.model.EPG;
import com.fortune.rms.business.live.model.Live;
import com.fortune.rms.business.live.model.Task;
import com.fortune.rms.business.system.logic.logicInterface.DeviceLogicInterface;
import com.fortune.rms.business.system.model.Device;
import com.fortune.server.message.ServerMessager;
import com.fortune.util.AppConfigurator;
import com.fortune.util.CacheUtils;
import com.fortune.util.StringUtils;
import com.fortune.util.TcpUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//import com.fortune.util.FileUtils;
//import com.fortune.util.net.URLEncoder;
//import org.apache.http.HttpEntity;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.protocol.HTTP;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.util.Enumeration;

/**
 * Created by 王明路 on 2015/2/26.
 * 直播Action
 */
@Namespace("/live")
@ParentPackage("default")
@Action(value = "epg")
public class EPGAction extends BaseAction<EPG> {
    public EPGAction() {
        super(EPG.class);
    }
    private EPGLogicInterface epgLogicInterface;

    public void setEpgLogicInterface(EPGLogicInterface epgLogicInterface) {
        this.epgLogicInterface = epgLogicInterface;
    }

}
