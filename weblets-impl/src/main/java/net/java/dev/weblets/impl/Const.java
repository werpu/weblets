package net.java.dev.weblets.impl;

import java.util.regex.Pattern;

/**
 * @author werpu
 * @date: 03.11.2008
 */
public class Const {
    public static final String REL_META_INF = "META-INF/";

    public static final String REL_WEB_INF = "WEB-INF/";
    public static final String FACES = "/faces/";
    public static final String BLANK = "";
    public static final String ALL = "*";
    public static final String SLASH = "/";
    public static final String ALLOWED_RESOURCES = "allowedResources";
    public static final String WEBLETS_CONFIG = "weblets-config";

    public static final int FIRST_PARAM = 0;
    public static final int SECOND_PARAM = 1;
    public static final int TWO_PARAMS = 2;

    public static final String WEB_XML = "web.xml";
    public static final String WEBLETS_CONFIG_XML = "weblets-config.xml";
    public static final String MANIFEST_MF = "MANIFEST.MF";
    public static final String CONTEXT_XML = "context.xml";
    public static final String ERR_REGISTER = "Unabled to register /WEB-INF/weblets-config.xml";
    public static final String ERR_PATHPATTERN_MISSING = "JSF Enabled Weblets but path pattern is missing, some relatively referenced resources might not load ";
    public static final String ERR_PATHPATTERNMISSING_2 = "Servlet Enabled Weblets but path pattern is missing, some relatively referenced resources might not load ";
    public static final String WEB_APP_SERVLET = "web-app/servlet";
    public static final String WEB_APP_SERVLET_MAPPING = "web-app/servlet-mapping";
    public static final String WEB_APP_CONTEXT_PARAM = "web-app/context-param";

    public static final String FUNC_ADD_SERVLET = "addServlet";
    public static final String FUNC_ADD_SERVLET_MAPPING = "addServletMapping";
    public static final String FUNC_ADD_CONTEXT_PARAM = "addContextParam";
    public static final String FUNC_ADD_SUBBUNDLE = "addSubbundle";

    public static final String PAR_WEB_INF = "/WEB-INF/";
    public static final String PAR_META_INF = "/META-INF/";
    public static final String PAR_SERVLET_NAME = "/servlet-name";
    public static final String PAR_SERVLET_CLASS = "/servlet-class";
    public static final String PAR_URL_PATTERN = "/url-pattern";
    public static final String PAR_PARAM_NAME = "/param-name";
    public static final String PAR_PARAM_VALUE = "/param-value";
    public static final String PAR_WEBLETS = "/weblets/";
    public static final String PAR_WEBLET = "/weblet";
    public static final String PAR_INIT_PARAM = "/init-param";
    public static final String PAR_MIME_MAPPING = "/mime-mapping";
    public static final String PAR_WEBLET_MAPPING = "/weblet-mapping";
    public static final String PAR_SUBBUNDLE = "/subbundle";
    public static final String PAR_WEBLET_NAME = "/weblet-name";
    public static final String PAR_WEBLET_CLASS = "/weblet-class";
    public static final String PAR_WEBLET_VERSION = "/weblet-version";
    public static final String PAR_EXTENSION = "/extension";
    public static final String PAR_MIME_TYPE = "/mime-type";
    public static final String PAR_ID = "/id";
    public static final String PAR_RESOURCES = "/resources";

    public static final Pattern PATTERN_WEBLET_PATH =
            Pattern.compile("(/[^\\*]+)?/\\*");
}
