package com.geeksworld.jktdvr.tools;

/**
 * Created by xhs on 2017/11/1.
 */

public class Url {
    public static final String BASE_HOST = "http://59.110.227.15:8888/";

    //public static final String BASE_HOST = "http://192.168.1.5:8888/";
    public static final String HOST = BASE_HOST+"ap/";

    public static final String URLSuffix = "";

    /*
    *1)注册 "phoneNumber":"18811470927",  "password":"123456"
    * */
    public static final String registUser = HOST + "user/save"+URLSuffix;

    /*
    * 2)短信验证码发送 "phoneNumber":"18811470927" //手机号
    * */
    public static final String sendPhoneVerifyCode = HOST + "user/sendValidateCode"+URLSuffix;

    /*
    * 3)验证短信验证码是否正确 "msg_id":"351991342573","verifycode":"716769"
    * */
    public static final String verificationCode = HOST + "verificationCode"+URLSuffix;

    /*
    * 4)用户登录输入手机号显示头像 user_name
    * */
    public static final String showUserPortrait = HOST + "showUserPortrait";

    /*
    *4)用户登录 "phoneNumber":"18811470927", "password":"123456"
    * */
    public static final String login = HOST + "user/login" +URLSuffix;

    /*手机号绑定微信*/
    public static String weixinBoundLogin = HOST + "user/vxBoundLogin"+URLSuffix;

    /*判断手机号是否已经绑定过*/
    public static String telephoneIsweixinBound = HOST + "user/telephoneIsweixinBound"+URLSuffix;

    /*6)根据微信code生成openid和access_token*/
    public static String weixinLogin = HOST + "user/thirdpartyVxLogin"+URLSuffix;


    /*手机号绑定QQ*/
    public static String QQBoundLogin = HOST + "user/qqBoundLogin"+URLSuffix;

    /*判断手机号是否已经绑定过QQ*/
    public static String telephoneIsQQBound = HOST + "user/telephoneIsqqBound"+URLSuffix;

    /*6)根据QQ code生成openid和access_token*/
    public static String QQLogin = HOST + "user/thirdpartyQQLogin"+URLSuffix;


    /*忘记密码"user_name":"18811470927", //用户手机号"user_pwd":"123456" //新密码  注：密码不能与原密码相同*/
    public static final String forgetPwd = HOST + "user/modifyPassword"+URLSuffix;

    /*8)根据用户id获取用户详情信息
    *  "uid":3
    * */
    public static final String displayUserDetails = HOST + "user/findById" +URLSuffix;

    /*9)完善用户详情或者修改
    *  "uid":3
    * */
    public static final String completeUserDetails = HOST + "user/save" + URLSuffix;

    /*9)修改用户头像
        *  "uid":3
        * */
    public static final String completeUserHeadImg = HOST + "user/updateUserHeadImg" +URLSuffix;


    public static final String postfileUpload = HOST + "img/fileUpload" +URLSuffix;


    public static final String postVRSave = HOST + "img/save" +URLSuffix;

    public static final String postVRdelete = HOST + "img/delete" +URLSuffix;


    /*9)获取首页数据
        *  "page":"1"//第几页
        "rows":"3"//每页记录数
        "title":"3"//标题，可根据标题模糊搜索
        * */
    public static final String showIndexPageMsg = HOST + "user/showIndexPageMsg" +URLSuffix;

    /*
    * 17)首页模糊查询
    * */
    public static final String fuzzyQuery = HOST + "user/fuzzyQuery" +URLSuffix;
    /*20)请求banner列表
        * */
    public static final String getBannerList = HOST + "banner/list" +URLSuffix;

    /*24)根据id返回文章详情
           * */
    public static final String getAticleDetail = HOST + "article/findById" +URLSuffix;


    /*9)阅读记录
        新增记录
        * */
    public static final String addNewReadingLog = HOST +"history/save" + URLSuffix;

    /*19)根据内容类型和用户id获取阅读记录
    "u_id":"3"//用户id
    "type":"3"//内容类型：1代表文章 2代表周报 3代表问卷
       * */
    public static final String getReadingLog = HOST +"history/findByTypeAndUserId" + URLSuffix;



    /*22)获取发布所有tag
        "dataDicValue":"article": //父类别的值，这里值传article
           * */
    public static final String getHomeAllTag = HOST +"dataDic/dataDicListByKeyName" + URLSuffix;
    /*21)请求发布列表
    "page":"3"//第几页
    "rows":"3"//每页记录数
    "tag":"分类tag"//分类tag
    "u_id":"用户id//用户id，用于查询用户已读的
           * */
    public static final String getHomeList = HOST +"img/list" + URLSuffix;

    /*
       *23)获取发布中的推荐数据列表
       * */
    public static final String getArticleFindRecommend = HOST +"article/findRecommend" + URLSuffix;

    /*
    * 27)根据art_id、u_id、type添加收藏
    * */
    public static final String collectSave = HOST +"collect/save" + URLSuffix;

    /*
    * 28)根据art_id、u_id、type取消收藏
    * */
    public static final String collectDeleteByArtIdAndTypeAndUserId = HOST +"collect/deleteByArtIdAndTypeAndUserId" + URLSuffix;


    /*
    * 31)根据art_id、u_id、type添加阅读记录
    *
    * */
    public static final String historySave = HOST +"history/save" + URLSuffix;
    /*
        * 34)根据art_id、u_id、type添加满意度
        *"art_id":3//文章id（Integer）
"u_id":3//用户id（Integer）
"type":"3"//类型（1文章2周报3问卷）（String）
"collect":"1"//1满意 2不满意（String）
        * */
    public static final String satisfySave = HOST +"satisfy/save" + URLSuffix;
    /*
       * 39)根据id返回周报详情
       *
       * */
    public static final String getWeeklyDetail = HOST +"weekly/findById" + URLSuffix;
    /*
           *
33)请求问卷列表
           *
           * */
    public static final String getQuestionList = HOST +"question/list" + URLSuffix;

    /*
    * 43)查询教育指数全部季度
    * */
    public static final String getEduNumQuarterList = HOST +"eduNum/quarterList" + URLSuffix;

    /*
    * 42)查询教育指数列表
    * */
    public static final String getEduNumList = HOST +"eduNum/list" + URLSuffix;


    /*
    * 44)获取教育指数最新数据
    * */
    public static final String getEduNumFindLastEduNum = HOST +"eduNum/findLastEduNum" + URLSuffix;
    /*
        * 38)获取最新一期周报
        * */
    public static final String getWeeklyFindLastWeekly = HOST +"weekly/findLastWeekly" + URLSuffix;
    /*
            * 37)请求周报列表
            * */
    public static final String getWeeklyList = HOST +"weekly/list" + URLSuffix;

    /*
            * 29)根据u_id、type查看收藏列表
            * */
    public static final String collectFindByTypeAndUserId = HOST +"collect/findByTypeAndUserId" + URLSuffix;

    /*
            * 30)根据ids集合删除收藏
            * */
    public static final String collectDelete = HOST +"collect/delete" + URLSuffix;

    /*
            * 29)根据u_id、type查看收藏列表
            * */
    public static final String historyFindByTypeAndUserId = HOST +"history/findByTypeAndUserId" + URLSuffix;


    /*
    *45)据根据问卷id返回问卷模块以及模块下题目集合
    * */
    public static final String questionFindQuesModuleProblemByQuesId = HOST +"question/findQuesModuleProblemByQuesId" + URLSuffix;

    /*
    *46)根据问卷下模块下题目的id返回题目对应的选项集合(针对非级联选项)
    * */
    public static final String questionFindOptionByProblemId = HOST +"question/findOptionByProblemId" + URLSuffix;

    /*
       *47)根据问卷下模块下题目的id返回题目对应的选项集合针对级联选项
       * */
    public static final String questionFindLevelOptionByProblemId = HOST +"question/findLevelOptionByProblemId" + URLSuffix;
    /*
           *50)已答问卷保存方法
           * */
    public static final String saveAnswerQuestion = HOST +"answer/answerQuestion" + URLSuffix;

    /*
           *49)获取最新关于我们
           * */
    public static final String aboutWeFindLastAboutWe = HOST +"aboutWe/findLastAboutWe" + URLSuffix;
    /*
           *48)获取最新版本号
           * */
    public static final String appVersionFindLastVersion = HOST +"appVersion/findLastVersion" + URLSuffix;


    /*
           *已答问卷
           * */
    public static final String answerAnswerList = HOST +"answer/answerList" + URLSuffix;

}
