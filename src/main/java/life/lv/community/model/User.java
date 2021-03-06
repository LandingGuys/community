package life.lv.community.model;

public class User {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user.id
     *
     * @mbg.generated Sat Sep 14 19:17:36 CST 2019
     */
    private Long id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user.account_id
     *
     * @mbg.generated Sat Sep 14 19:17:36 CST 2019
     */
    private String accountId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user.name
     *
     * @mbg.generated Sat Sep 14 19:17:36 CST 2019
     */
    private String name;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user.password
     *
     * @mbg.generated Sat Sep 14 19:17:36 CST 2019
     */
    private String password;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user.token
     *
     * @mbg.generated Sat Sep 14 19:17:36 CST 2019
     */
    private String token;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user.gmt_create
     *
     * @mbg.generated Sat Sep 14 19:17:36 CST 2019
     */
    private Long gmtCreate;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user.gmt_modified
     *
     * @mbg.generated Sat Sep 14 19:17:36 CST 2019
     */
    private Long gmtModified;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user.avatar_url
     *
     * @mbg.generated Sat Sep 14 19:17:36 CST 2019
     */
    private String avatarUrl;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user.user_name
     *
     * @mbg.generated Sat Sep 14 19:17:36 CST 2019
     */
    private String userName;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user.user_region
     *
     * @mbg.generated Sat Sep 14 19:17:36 CST 2019
     */
    private String userRegion;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user.user_industry
     *
     * @mbg.generated Sat Sep 14 19:17:36 CST 2019
     */
    private String userIndustry;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user.user_introduction
     *
     * @mbg.generated Sat Sep 14 19:17:36 CST 2019
     */
    private String userIntroduction;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user.id
     *
     * @return the value of user.id
     *
     * @mbg.generated Sat Sep 14 19:17:36 CST 2019
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user.id
     *
     * @param id the value for user.id
     *
     * @mbg.generated Sat Sep 14 19:17:36 CST 2019
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user.account_id
     *
     * @return the value of user.account_id
     *
     * @mbg.generated Sat Sep 14 19:17:36 CST 2019
     */
    public String getAccountId() {
        return accountId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user.account_id
     *
     * @param accountId the value for user.account_id
     *
     * @mbg.generated Sat Sep 14 19:17:36 CST 2019
     */
    public void setAccountId(String accountId) {
        this.accountId = accountId == null ? null : accountId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user.name
     *
     * @return the value of user.name
     *
     * @mbg.generated Sat Sep 14 19:17:36 CST 2019
     */
    public String getName() {
        return name;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user.name
     *
     * @param name the value for user.name
     *
     * @mbg.generated Sat Sep 14 19:17:36 CST 2019
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user.password
     *
     * @return the value of user.password
     *
     * @mbg.generated Sat Sep 14 19:17:36 CST 2019
     */
    public String getPassword() {
        return password;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user.password
     *
     * @param password the value for user.password
     *
     * @mbg.generated Sat Sep 14 19:17:36 CST 2019
     */
    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user.token
     *
     * @return the value of user.token
     *
     * @mbg.generated Sat Sep 14 19:17:36 CST 2019
     */
    public String getToken() {
        return token;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user.token
     *
     * @param token the value for user.token
     *
     * @mbg.generated Sat Sep 14 19:17:36 CST 2019
     */
    public void setToken(String token) {
        this.token = token == null ? null : token.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user.gmt_create
     *
     * @return the value of user.gmt_create
     *
     * @mbg.generated Sat Sep 14 19:17:36 CST 2019
     */
    public Long getGmtCreate() {
        return gmtCreate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user.gmt_create
     *
     * @param gmtCreate the value for user.gmt_create
     *
     * @mbg.generated Sat Sep 14 19:17:36 CST 2019
     */
    public void setGmtCreate(Long gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user.gmt_modified
     *
     * @return the value of user.gmt_modified
     *
     * @mbg.generated Sat Sep 14 19:17:36 CST 2019
     */
    public Long getGmtModified() {
        return gmtModified;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user.gmt_modified
     *
     * @param gmtModified the value for user.gmt_modified
     *
     * @mbg.generated Sat Sep 14 19:17:36 CST 2019
     */
    public void setGmtModified(Long gmtModified) {
        this.gmtModified = gmtModified;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user.avatar_url
     *
     * @return the value of user.avatar_url
     *
     * @mbg.generated Sat Sep 14 19:17:36 CST 2019
     */
    public String getAvatarUrl() {
        return avatarUrl;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user.avatar_url
     *
     * @param avatarUrl the value for user.avatar_url
     *
     * @mbg.generated Sat Sep 14 19:17:36 CST 2019
     */
    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl == null ? null : avatarUrl.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user.user_name
     *
     * @return the value of user.user_name
     *
     * @mbg.generated Sat Sep 14 19:17:36 CST 2019
     */
    public String getUserName() {
        return userName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user.user_name
     *
     * @param userName the value for user.user_name
     *
     * @mbg.generated Sat Sep 14 19:17:36 CST 2019
     */
    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user.user_region
     *
     * @return the value of user.user_region
     *
     * @mbg.generated Sat Sep 14 19:17:36 CST 2019
     */
    public String getUserRegion() {
        return userRegion;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user.user_region
     *
     * @param userRegion the value for user.user_region
     *
     * @mbg.generated Sat Sep 14 19:17:36 CST 2019
     */
    public void setUserRegion(String userRegion) {
        this.userRegion = userRegion == null ? null : userRegion.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user.user_industry
     *
     * @return the value of user.user_industry
     *
     * @mbg.generated Sat Sep 14 19:17:36 CST 2019
     */
    public String getUserIndustry() {
        return userIndustry;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user.user_industry
     *
     * @param userIndustry the value for user.user_industry
     *
     * @mbg.generated Sat Sep 14 19:17:36 CST 2019
     */
    public void setUserIndustry(String userIndustry) {
        this.userIndustry = userIndustry == null ? null : userIndustry.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user.user_introduction
     *
     * @return the value of user.user_introduction
     *
     * @mbg.generated Sat Sep 14 19:17:36 CST 2019
     */
    public String getUserIntroduction() {
        return userIntroduction;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user.user_introduction
     *
     * @param userIntroduction the value for user.user_introduction
     *
     * @mbg.generated Sat Sep 14 19:17:36 CST 2019
     */
    public void setUserIntroduction(String userIntroduction) {
        this.userIntroduction = userIntroduction == null ? null : userIntroduction.trim();
    }
}