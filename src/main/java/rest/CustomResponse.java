package rest;

import java.util.List;
import java.util.Map;
import io.restassured.response.Response;

public class CustomResponse {

    private Response response;
    private int statusCode;
    private String status;
    private String statusLine;

    // Theme API fields
    private String primaryColor;
    private String primaryFontColor;
    private String secondaryColor;

    // Email configuration fields
    private String mailType;
    private String sentAs;
    private String smtpAuthType;
    private String smtpHost;
    private String smtpUsername;

    // Common fields for list-based APIs
    private List<Object> ids;
    private List<Object> unitIds;
    private List<Object> names;
    private List<Object> levels;
    private List<Object> children;
    private List<Object> descriptions;
    private Map<String, Object> meta;

    // Field for postId used in some constructors
    private int postId;

    // Field for employeeDetails used in some constructors
    private Map<String, Object> employeeDetails;

    // Field for rawResponse used in some constructors
    private String rawResponse;

    // Email subscription API fields
    private List<Integer> idList;
    private List<String> nameList;
    private List<Boolean> isEnabledList;
    private Map<String, Object> metaMap;

    // ===== Constructors =====

    // Constructor for getSubunitsTree
    public CustomResponse(Response response, int statusCode, String status,
                          List<Object> ids, List<Object> unitIds,
                          List<Object> names, List<Object> levels,
                          List<Object> children) {
        this.response = response;
        this.statusCode = statusCode;
        this.status = status;
        this.ids = ids;
        this.unitIds = unitIds;
        this.names = names;
        this.levels = levels;
        this.children = children;
    }

    // Constructor for list with meta
    public CustomResponse(Response response, int statusCode, String status,
                          List<Object> ids, List<Object> names,
                          Map<String, Object> meta) {
        this.response = response;
        this.statusCode = statusCode;
        this.status = status;
        this.ids = ids;
        this.names = names;
        this.meta = meta;
    }

    // Constructor for getAdminSkills
    public CustomResponse(Response response, int statusCode, String status,
                          List<Object> ids, List<Object> names,
                          List<Object> descriptions) {
        this.response = response;
        this.statusCode = statusCode;
        this.status = status;
        this.ids = ids;
        this.names = names;
        this.descriptions = descriptions;
    }

    // Constructor for simple ID + Name lists
    public CustomResponse(Response response, int statusCode, String status,
                          List<Object> ids, List<Object> names) {
        this.response = response;
        this.statusCode = statusCode;
        this.status = status;
        this.ids = ids;
        this.names = names;
    }

    // Constructor for Theme API
    public CustomResponse(String status, int statusCode,
                          String primaryColor, String primaryFontColor, String secondaryColor) {
        this.statusCode = statusCode;
        this.status = status;
        this.primaryColor = primaryColor;
        this.primaryFontColor = primaryFontColor;
        this.secondaryColor = secondaryColor;
    }

    // Constructor for Email Configuration API
    public CustomResponse(String status, int statusCode, String mailType, String sentAs,
                          String smtpAuthType, String smtpHost, String smtpUsername) {
        this.status = status;
        this.statusCode = statusCode;
        this.mailType = mailType;
        this.sentAs = sentAs;
        this.smtpAuthType = smtpAuthType;
        this.smtpHost = smtpHost;
        this.smtpUsername = smtpUsername;
    }

    // Constructor for Email Subscription API
    public CustomResponse(String status, int statusCode,
                          List<Integer> idList, List<String> nameList,
                          List<Boolean> isEnabledList, Map<String, Object> metaMap) {
        this.status = status;
        this.statusCode = statusCode;
        this.idList = idList;
        this.nameList = nameList;
        this.isEnabledList = isEnabledList;
        this.metaMap = metaMap;
    }

    public CustomResponse(Response response, int statusCode, String statusLine) {
    this.response = response;
    this.statusCode = statusCode;
    this.statusLine = statusLine;
}

public CustomResponse(int statusCode, String statusLine, int postId, Map<String, Object> employeeDetails, String rawResponse) {
    this.statusCode = statusCode;
    this.statusLine = statusLine;
    this.postId = postId;  // create an int field postId in your class
    this.employeeDetails = employeeDetails; // create a Map<String, Object> field
    this.rawResponse = rawResponse;
}

// Constructor for PostPhoto API response
public CustomResponse(int statusCode, String statusLine, String postIdStr, 
                      Map<String, Object> employeeDetails, String rawResponse) {
    this.statusCode = statusCode;
    this.statusLine = statusLine;
    try {
        this.postId = Integer.parseInt(postIdStr);
    } catch (NumberFormatException e) {
        this.postId = -1; // fallback if parsing fails
    }
    this.employeeDetails = employeeDetails;
    this.rawResponse = rawResponse;
}


// Constructor for PostPhoto API
public CustomResponse(int statusCode, String statusLine, int postId, 
                      String postType, Map<String, Object> employeeDetails, 
                      String rawResponse) {
    this.statusCode = statusCode;
    this.statusLine = statusLine;
    this.postId = postId;
    this.employeeDetails = employeeDetails;
    this.rawResponse = rawResponse;
    // Optionally store post type if you want to assert it later
    if (employeeDetails != null) {
        this.status = postType; // Not HTTP status — reusing field for post type
    }
}

public CustomResponse(String rawResponse, int statusCode, String statusLine) {
        this.rawResponse = rawResponse;
        this.statusCode = statusCode;
        this.statusLine = statusLine;
    }





    

    // ===== Getters =====

    // Theme
    public String getPrimaryColor() { return primaryColor; }
    public String getPrimaryFontColor() { return primaryFontColor; }
    public String getSecondaryColor() { return secondaryColor; }

    // Email configuration
    public String getMailType() { return mailType; }
    public String getSentAs() { return sentAs; }
    public String getSmtpAuthType() { return smtpAuthType; }
    public String getSmtpHost() { return smtpHost; }
    public String getSmtpUsername() { return smtpUsername; }

    // Common
    public Response getResponse() { return response; }
    public void setResponse(Response response) { this.response = response; }
    public int getStatusCode() { return statusCode; }
    public void setStatusCode(int statusCode) { this.statusCode = statusCode; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public List<Object> getIds() { return ids; }
    public void setIds(List<Object> ids) { this.ids = ids; }
    public List<Object> getUnitIds() { return unitIds; }
    public void setUnitIds(List<Object> unitIds) { this.unitIds = unitIds; }
    public List<Object> getNames() { return names; }
    public void setNames(List<Object> names) { this.names = names; }
    public List<Object> getLevels() { return levels; }
    public void setLevels(List<Object> levels) { this.levels = levels; }
    public List<Object> getChildren() { return children; }
    public void setChildren(List<Object> children) { this.children = children; }
    public List<Object> getDescriptions() { return descriptions; }
    public void setDescriptions(List<Object> descriptions) { this.descriptions = descriptions; }
    public Map<String, Object> getMeta() { return meta; }
    public void setMeta(Map<String, Object> meta) { this.meta = meta; }
    public String getStatusLine() { return statusLine; }
     public String getRawResponse() { return this.rawResponse; }

    // Email subscription
    public List<Integer> getIdList() { return idList; }
    public List<String> getNameList() { return nameList; }
    public List<Boolean> getIsEnabledList() { return isEnabledList; }
    public Map<String, Object> getMetaMap() { return metaMap; }

    
}
