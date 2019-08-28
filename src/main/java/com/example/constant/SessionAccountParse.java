package com.example.constant;

import java.lang.reflect.Type;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

//import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

//import com.his.sdk.pojo.Return;
//import com.his.sdk.util.GsonUtil;
//import com.his.sdk.util.NetUtil;
//import com.his.session.constant.SessionEnumError;
//import com.his.session.data.Session;
//import com.his.session.pojo.accept.AbstractAccept;
//import com.his.session.pojo.accept.SessionAccept;
//import com.his.session.pojo.bo.BaseNet;

@Component
public class SessionAccountParse {
//	private static final Logger logger = LoggerFactory.getLogger(SessionAccountParse.class);

    @Resource
    private RedisAccount redisAccount;

    /**
     * 网络数据解析、验请参数
     * @param request
     * @param c
     * @return
     */
    public static <T extends AbstractAccept> Return<T> parseNo(HttpServletRequest request, Type c) {
        // 解析数据
        T param = baseParse(request, c);

        // 判断是否解析成功
        if (param == null) {
            return Return.createNew(SessionEnumError.UNPACK_ERROR.getCode(), SessionEnumError.UNPACK_ERROR.getDesc());
        }

        // 验证参数
        if (!param.checkClientIP()) {
            return Return.createNew(SessionEnumError.PARAM_ERROR.getCode(), SessionEnumError.PARAM_ERROR.getDesc());
        }

        return Return.createNew(param);
    }

    public Return<Session> checkSession(String token) {
        // 登入验证
        Session session = redisAccount.getAndFlushSessionCache(token);
        if (session == null) {
            return Return.createNew(SessionEnumError.PLEASE_LOGIN_AGAIN_ERROR.getCode(), SessionEnumError.PLEASE_LOGIN_AGAIN_ERROR.getDesc());
        }
//		if (!param.getClientIP().equals(session.getClientIP())) {
//			logger.error("param clientIP=" + param.getClientIP() + ",sessionIP=" + session.getClientIP());
//			return Return.createNew(SessionEnumError.IP_ERROR.getCode(), SessionEnumError.IP_ERROR.getDesc());
//		}
        if (!session.checkLogin()) {
            return Return.createNew(SessionEnumError.PLEASE_LOGIN_ERROR.getCode(), SessionEnumError.PLEASE_LOGIN_ERROR.getDesc());
        }

        return Return.createNew(session);
    }

    /**
     * 网络数据解析、验请参数、登入验证、权限验证
     * @param request
     * @param c
     * @return
     */
    public <T extends AbstractAccept> Return<T> parseLogin(HttpServletRequest request, Type c) {
        // 解析数据
        T param = baseParse(request, c);

        // 判断是否解析成功
        if (param == null) {
            return Return.createNew(SessionEnumError.UNPACK_ERROR.getCode(), SessionEnumError.UNPACK_ERROR.getDesc());
        }

        // 验证参数
        if (!param.check()) {
            return Return.createNew(SessionEnumError.PARAM_ERROR.getCode(), SessionEnumError.PARAM_ERROR.getDesc());
        }

        // 登入验证
        Session session = redisAccount.getAndFlushSessionCache(param.getToken());
        if (session == null) {
            return Return.createNew(SessionEnumError.PLEASE_LOGIN_AGAIN_ERROR.getCode(), SessionEnumError.PLEASE_LOGIN_AGAIN_ERROR.getDesc());
        }
//		if (!param.getClientIP().equals(session.getClientIP())) {
//			logger.error("param clientIP=" + param.getClientIP() + ",sessionIP=" + session.getClientIP());
//			return Return.createNew(SessionEnumError.IP_ERROR.getCode(), SessionEnumError.IP_ERROR.getDesc());
//		}
        if (!session.checkLogin()) {
            return Return.createNew(SessionEnumError.PLEASE_LOGIN_ERROR.getCode(), SessionEnumError.PLEASE_LOGIN_ERROR.getDesc());
        }
        param.setSession(session);

        return Return.createNew(param);
    }

    /**
     * 网络数据解析、验请参数、登入验证、权限验证
     * @param request
     * @param c
     * @return
     */
    public <T extends AbstractAccept> Return<T> parse(HttpServletRequest request, Type c) {
        // 解析数据
        T param = baseParse(request, c);

        // 判断是否解析成功
        if (param == null) {
            return Return.createNew(SessionEnumError.UNPACK_ERROR.getCode(), SessionEnumError.UNPACK_ERROR.getDesc());
        }

        // 验证参数
        if (!param.check()) {
            return Return.createNew(SessionEnumError.PARAM_ERROR.getCode(), SessionEnumError.PARAM_ERROR.getDesc());
        }

        // 登入验证
        Session session = redisAccount.getAndFlushSessionCache(param.getToken());
        if (session == null) {
            return Return.createNew(SessionEnumError.PLEASE_LOGIN_AGAIN_ERROR.getCode(), SessionEnumError.PLEASE_LOGIN_AGAIN_ERROR.getDesc());
        }
//		if (!param.getClientIP().equals(session.getClientIP())) {
//			logger.error("param clientIP=" + param.getClientIP() + ",sessionIP=" + session.getClientIP());
//			return Return.createNew(SessionEnumError.IP_ERROR.getCode(), SessionEnumError.IP_ERROR.getDesc());
//		}
        if (!session.checkLogin()) {
            return Return.createNew(SessionEnumError.PLEASE_LOGIN_ERROR.getCode(), SessionEnumError.PLEASE_LOGIN_ERROR.getDesc());
        }
        param.setSession(session);


        // 权限验证
        String uri = request.getServletPath();
        if (!session.checkResource(uri)) {
            return Return.createNew(SessionEnumError.POWER_ERROR.getCode(), SessionEnumError.POWER_ERROR.getDesc());
        }

        return Return.createNew(param);
    }

    /**
     * 网络数据解析、验请参数、登入验证、权限验证
     * @param request
     * @param c
     * @return
     */
    public <T extends SessionAccept> Return<T> parseClinicLogin(HttpServletRequest request, Type c) {
        Return<T> ret = parseLogin(request, c);
        if (Return.isErr(ret)) {
            return ret;
        }

        T t = ret.getData();
        if (!t.checkClinic()) {
            return Return.createNew(SessionEnumError.NO_COMEIN_CLINIC_ERROR.getCode(), SessionEnumError.NO_COMEIN_CLINIC_ERROR.getDesc());
        }

        return ret;
    }

    /**
     * 网络数据解析、验请参数、登入验证、权限验证
     * @param request
     * @param c
     * @return
     */
    public <T extends SessionAccept> Return<T> parseClinic(HttpServletRequest request, Type c) {
        Return<T> ret = parse(request, c);
        if (Return.isErr(ret)) {
            return ret;
        }

        T t = ret.getData();
        if (!t.checkClinic()) {
            return Return.createNew(SessionEnumError.NO_COMEIN_CLINIC_ERROR.getCode(), SessionEnumError.NO_COMEIN_CLINIC_ERROR.getDesc());
        }

        t.getSession().mysetMenuId(t.getMenuId());

        return ret;
    }


    /**
     * 请求数据解析
     * @param request
     * @param c
     * @return
     */
    public final static <T extends AbstractAccept> T baseParse(HttpServletRequest request, Type c) {
        T t = null;
        try {
            String clientIP = NetUtil.getIpAddress(request);
            if (StringUtils.isBlank(clientIP)) {
                return null;
            }

            String data = NetUtil.parse(request);
            if (data == null || data.length() == 0) {
                return null;
            }

            if (StringUtils.isBlank(data) || data.length() < 2) {
                //logger.warn("encode error : " + data);
                return null;
            }

            BaseNet baseNet = GsonUtil.toJson(data, BaseNet.class);
            int encrypt = baseNet.getEncrypt();
            String token = baseNet.getToken();
            long timestamp = baseNet.getTimestamp();
            String sign = baseNet.getSign();
            String str = baseNet.getData();
            if (timestamp <= 0 || encrypt < 0 || StringUtils.isBlank(str) || str.length() < 2) {
                return null;
            }
//	    	writeInFileByfb(data);
//	    	JSONObject jsonObject = JSONObject.parseObject(data);
//			if (jsonObject == null) {
//				return null;
//			}
//
//			int encrypt = (Integer)jsonObject.get("encrypt");
//			String token = (String)jsonObject.get("token");
//			long timestamp = (Long)jsonObject.get("timestamp");
//			String sign = (String)jsonObject.get("sign");
//			Object obj = jsonObject.get("data");
//
//			if (obj == null || timestamp <= 0 || encrypt < 0) {
//				return null;
//			}
//
//			String str = obj.toString();
//			if (StringUtils.isBlank(str) || str.length() < 2) {
//				return null;
//			}
            t = GsonUtil.toJson(str, c);
            if (t == null) {
                return null;
            }

            t.setClientIP(clientIP);
            t.setEncrypt(encrypt);
            t.setToken(token);
            t.setTimestamp(timestamp);
            t.setSign(sign);
        } catch (Exception e) {
            //logger.warn(e.getMessage());
            return null;
        }
        return t;
    }

//	public static void writeInFileByfb(String data) {
//        File f=new File("F:/mylog.log");
//        FileWriter fw=null;
//        BufferedWriter bw=null;
//        try{
//            if(!f.exists()){
//                f.createNewFile();
//            }
//             fw=new FileWriter(f.getAbsoluteFile(),true);  //true表示可以追加新内容
//                         //fw=new FileWriter(f.getAbsoluteFile()); //表示不追加
//             bw=new BufferedWriter(fw);
//             bw.write(data);
//             bw.close();
//        }catch(Exception e){
//           e.printStackTrace();
//        }
//
//    }
}
