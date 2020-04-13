
$(function () {
    var action="dynamic";
    if($("#profile_action").val()!=null&& $("#profile_action").val()!=""){
        action=$("#profile_action").val();
    }
    var userId=$("#user_id").val();
    //到第几页,默认到第一页
    $(".nav_list li").removeClass("active");
    $("#"+action+"").parent().addClass("active");

    to_dynamic_page(1,action,userId);

    build_right_list();
});


//分页
function build_page_nav(page,totalCount,action,userId){
    $("#myPage").sPage({
        page:page,//当前页码，必填
        total:totalCount,//数据总条数，必填
        pageSize:10,//每页显示多少条数据，默认10条
        totalTxt:"共{total}条",//数据总条数文字描述，{total}为占位符，默认"共{total}条"
        noData: false,//没有数据时是否显示分页，默认false不显示，true显示第一页
        showTotal:true,//是否显示总条数，默认关闭：false
        showSkip:true,//是否显示跳页，默认关闭：false
        showPN:true,//是否显示上下翻页，默认开启：true
        prevPage:"上一页",//上翻页文字描述，默认“上一页”
        nextPage:"下一页",//下翻页文字描述，默认“下一页”
        backFun:function(page){
            //点击分页按钮回调函数，返回当前页码
            // if(action=="question"){
                to_question_page(page,action,userId);
            // }else if(action=="dynamic"){
            //     to_dynamic_page(page,action,userId);
            // }

        }
    });
}
//构建右侧数据
function build_right_list() {
    $.ajax({
        type: "GET",
        url: "/loadRightList",
        data: {
            "time": new Date()
        },
        success: function (res) {
            if (res.code == "200") {
                build_user_list(res);
                build_hot_tags(res);
                build_new_questions(res);
                build_recommend_questions(res);
            } else {

            }
        }
    })
}

function build_new_questions(data) {
    $("#newquestions_wrapper").empty();
    $.each(data.extend.newQuestions, function (index, item) {
        var li = $("<li class='list-group-item'  class=\"\"><a title='查看全文' href='/question/" + item.id + "'>" + item.title + "</a></li>");
        li.appendTo($("#newquestions_wrapper"));
    });
}

function build_recommend_questions(data) {
    $("#recommend_wrapper").empty();
    $.each(data.extend.recommend.data, function (index, item) {
        var li = $("<li class='list-group-item'  class=\"\"><span id='hit_no_"+index+"'>"+(index+1)+"</span><a  title='查看全文' href='/question/" + item.id + "' >" + item.title + "</a>" + "</li>");
        li.appendTo($("#recommend_wrapper"));
    });
}

function build_hot_tags(data) {
    $(".tag_wrapper").empty();
    $.each(data.extend.tags, function (index, item) {
        var hot_tag = $("  <div class=\"hot_tag\">\n" +
            "            <a  class='tagname' id='" + item + "' onclick='setTag(this)' ><span>" + item + "</span></a>\n" +
            "        </div>");
        hot_tag.appendTo($(".tag_wrapper"));
    });
}

function setTag(e) {
    $("#tag_param").val(e.id);
    $(".tagname").css({color: "#fff"})
    e.style.color = "#337ab7";
    to_page(1);
    return false;
}

function build_user_list(data) {
    //清空
    $("#newUser_wrapper").empty();
    var users = data.extend.userNewList;
    $.each(users, function (index, item) {
        var userdiv=$(" <div data-v-f340ffc8 class=\"user-media text-center d-inline-block p-1\">\n" +
            "               <a href=\"/user/"+item.id +"\" class>\n" +
            "                 <img src=\""+item.avatarUrl+"\" alt=\""+item.name+"\" class=\"avatar-40\" />\n" +
            "               </a>\n" +
            "           </div>")
        userdiv.appendTo("#newUser_wrapper");
    });
}
//加载个人动态
function to_dynamic_page(page,action,userId) {

    //加载完成之后,发送请求到服务器,拿到jason数据,构建列表数据
    var url = "/user/loadUserData/"+action;
    $.ajax({
        type: "GET",
        url: url,
        data: {
            "page": page,
            "pageNum": 10,
            "userId":userId,
            contentType: "application/json;charset=UTF-8"
        },
        beforeSend: function () {
            //loadingIndex = layer.msg('加载数据~~', {icon: 16});
            NProgress.start();
        },
        success: function (data) {
            //layer.close(loadingIndex);
            NProgress.done();
            if (data.code == "200") {
                if(data.data.totalPage>1){
                    //构造个人动态列表信息
                    build_dynamic_list(data);
                    //构建分页信息
                    build_page_nav(data.data.page,data.data.totalCount,action,userId);
                }else if(data.data.totalPage==1){
                    build_dynamic_list(data);
                }else{
                    //构造空的问题列表
                    build_question_empty_list();
                }
                $("html,body").animate({scrollTop: 0}, 0);//回到顶端
            } else {
                //提示报错信息
            }
        }
    });

}


//到问题页第几页
function to_question_page(page,action,userId) {

    //加载完成之后,发送请求到服务器,拿到jason数据,构建列表数据
    var url = "/user/loadUserData/"+action;
    $.ajax({
        type: "GET",
        url: url,
        data: {
            "page": page,
            "pageNum": 10,
            "userId":userId,
            contentType: "application/json;charset=UTF-8"
        },
        beforeSend: function () {
            //loadingIndex = layer.msg('加载数据~~', {icon: 16});
            NProgress.start();
        },
        success: function (data) {
            //layer.close(loadingIndex);
            NProgress.done();
            if (data.code == "200") {
                if(data.data.totalPage>1){
                    //构建问题列表信息
                    build_question_list(data);
                    //构建分页信息
                    build_page_nav(data.data.page,data.data.totalCount,action,userId);
                }else if(data.data.totalPage==1){
                    build_question_list(data);


                }else{
                    //构造空的问题列表
                    build_question_empty_list();
                }
                $("html,body").animate({scrollTop: 0}, 0);//回到顶端
            } else {
                //提示报错信息
            }
        }
    });

}
//时间戳转化
Date.prototype.Format = function (fmt) {
    var o = {
        "M+": this.getMonth() + 1, // 月份
        "d+": this.getDate(), // 日
        "h+": this.getHours(), // 小时
        "m+": this.getMinutes(), // 分
        "s+": this.getSeconds(), // 秒
        "q+": Math.floor((this.getMonth() + 3) / 3), // 季度
        "S": this.getMilliseconds() // 毫秒
    };
    if (/(y+)/.test(fmt))
        fmt = fmt.replace(RegExp.$1, (this.getFullYear() + ""));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}



//构建questions列表
function build_question_list(data) {
    //清空
    $("#profiledata").empty();
    var questions = data.data.data;
    $.each(questions, function (index, item) {
        var question=$("<div  class=\"media question\">\n" +
            "                    <div class=\"media-left\">\n" +
            "                        <a href=\"/user/"+item.user.id+"\">\n" +
            "                            <img class=\"media-object img-rounded\" src=\""+item.user.avatarUrl+"\" >\n" +
            "                        </a>\n" +
            "                    </div>\n" +
            "                    <div class=\"media-body\">\n" +
            "                        <h5 class=\"media-heading\">\n" +
            "                            <a href=\"/question/"+item.id+"\">"+item.title+"</a>\n" +
            "                        </h5>\n" +
            "                        <span class=\"text-color-999\"> • <span >"+item.user.name+"</span> • <span >"+new Date(item.gmtCreate).Format('yy-MM-dd hh:mm')+"</span></span>\n" +
            "                    </div>\n" +
            "                    <div class=\"media-right\" style=\"float:right\">\n" +
            "                        <span class=\"glyphicon glyphicon-heart\"></span>\n" +
            "                        <span class=\"idata\"><span >"+item.commentCount+"</span></span>\n" +
            "                        <span class=\"glyphicon glyphicon-comment\"></span>\n" +
            "                            <span class=\"idata\"><span >"+item.commentCount+"</span></span>\n" +
            "                        <span class=\"glyphicon glyphicon-eye-open\"></span>\n" +
            "                            <span class=\"idata\"><span >"+item.viewCount+"</span></span>\n" +
            "                    </div>\n" +
            "\n" +
            "                </div>")

        question.appendTo("#profiledata");
    })
    var fenye=$("<div class=\"page-control\" id=\"myPage\" style=\"margin-bottom: 20px;margin-top: 20px\">\n" +
        "                </div>")

    fenye.appendTo("#profiledata");
}

//构建空列表
function build_question_empty_list() {
    $("#profiledata").empty();
    var question=$(" <div style=\"padding-left: 320px;padding-top: 200px;padding-bottom: 200px;\n" +
        "                background-color: white\">\n" +
        "\n" +
        "                    <svg class=\"icon\" aria-hidden=\"true\" style=\"font-size: 100px;\">\n" +
        "                        <use xlink:href=\"#icon-sn-discuss-copy\"></use>\n" +
        "                    </svg>\n" +
        "                    <p>\n" +
        "                        该分类下无相关讨论哦~\n" +
        "                    </p>\n" +
        "                </div>")
    question.appendTo("#profiledata");
}

//构建个人动态
function build_dynamic_list(data) {
    //清空
    $("#profiledata").empty();
    var dynamics = data.data.data;
    $.each(dynamics, function (index, item){
        var dynamic=$(" <div >\n" +
            "                    <div>\n" +
            "                        <a href=\"/user/"+item.user.id+"\">\n" +
            "                           <img src=\""+item.user.avatarUrl+"\" alt=\"修改头像\" class=\"img-circle\" width=\"50px\" height=\"50px\" >\n" +
            "                        </a>\n" +
            "                        <div style=\"margin-left: 65px;margin-top: -40px;\">\n" +
            "                            <span>"+item.notifierName+"</span>\n" +
            "                            <span>"+item.typeName+"</span>\n" +
            "                            <a href=\"/notification/dynamic/"+item.id+"\">"+item.outerTitle+"</a>\n" +
            "                        </div>\n" +
            "                        <div style=\"margin-left: 65px\">\n" +
            "                        <span style=\"color: #777777;font-size: 12px\">\n" +
            "                            "+new Date(item.gmtCreate).Format('yy-MM-dd hh:mm')+"\n" +
            "                        </span>\n" +
            "                        </div>\n" +
            "                    </div>\n" +
            "\n" +
            "                    <div style=\"border-left: 2px solid #e4e4e4;\n" +
            "                            margin-top: 15px;\n" +
            "                            margin-left: 25px;\n" +
            "                            margin-bottom: 15px;\">\n" +
            "                        <div style=\"background-color: white;margin-left: 70px;\">\n" +
            "                            <a href=\"/notification/dynamic/"+item.id+"\">"+
            "                            <h4 style=\"padding: 22px;\">\n" +
                                                item.outerContent+
            "                                "+
            "                            </h4>\n" +
            "                           </a>\n" +
            "                        </div>\n" +
            "                    </div>\n" +
            "                </div>" )
        dynamic.appendTo("#profiledata");
    })

    var fenye=$("<div class=\"page-control\" id=\"myPage\" style=\"margin-bottom: 20px;margin-top: 20px\">\n" +
        "                </div>")

    fenye.appendTo("#profiledata");
}
