$(function () {
    // //总记录数
    // var totalpageo;
    // //当前页
    // var currentpage;
    // //到第几页,默认到第一页
     to_page(1);
    //加载右边的数据
    build_right_list();
});


//分页
function build_page_nav(page,totalCount){
    $("#myPage").sPage({
        page:page,//当前页码，必填
        total:totalCount,//数据总条数，必填
        pageSize:15,//每页显示多少条数据，默认10条
        totalTxt:"共{total}条",//数据总条数文字描述，{total}为占位符，默认"共{total}条"
        noData: false,//没有数据时是否显示分页，默认false不显示，true显示第一页
        showTotal:true,//是否显示总条数，默认关闭：false
        showSkip:true,//是否显示跳页，默认关闭：false
        showPN:true,//是否显示上下翻页，默认开启：true
        prevPage:"上一页",//上翻页文字描述，默认“上一页”
        nextPage:"下一页",//下翻页文字描述，默认“下一页”
        backFun:function(page){
            //点击分页按钮回调函数，返回当前页码
            to_page(page)
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

//到问题页第几页
function to_page(page) {

    //加载完成之后,发送请求到服务器,拿到jason数据,构建列表数据
    var url = "/loadIndex";
    $.ajax({
        type: "GET",
        url: url,
        data: {
            "page": page,
            "pageNum": 15,
            "tag": $("#tag_param").val(),
            "search": $("#search_param").val(),
            "sort": $("#sortby_param").attr("name"),
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
                if(data.data.totalPage>0){
                    //构建问题列表信息
                    build_question_list(data);
                    //构建分页信息
                    build_page_nav(data.data.page,data.data.totalCount);
                }else{
                    build_question_empty_list();

                }
                $("html,body").animate({scrollTop: 0}, 0);//回到顶端
            } else {

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
    $("#question_wrapper").empty();
    var questions = data.data.data;
    $.each(questions, function (index, item) {
        var question=$("<div class=\"media question\">\n" +
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

        question.appendTo("#question_wrapper");
    })
    var fenye=$("<div class=\"page-control\" id=\"myPage\" style=\"margin-bottom: 20px;margin-top: 20px\">\n" +
        "                </div>")

    fenye.appendTo("#question_wrapper");
}
//构建空列表
function build_question_empty_list() {
    $("#question_wrapper").empty();
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
    question.appendTo("#question_wrapper");
}
// function siteTime() {
//     window.setTimeout("siteTime()", 1000);
//     var seconds = 1000;
//     var minutes = seconds * 60;
//     var hours = minutes * 60;
//     var days = hours * 24;
//     var years = days * 365;
//     var today = new Date();
//     var todayYear = today.getFullYear();
//     var todayMonth = today.getMonth() + 1;
//     var todayDate = today.getDate();
//     var todayHour = today.getHours();
//     var todayMinute = today.getMinutes();
//     var todaySecond = today.getSeconds();
//     /* Date.UTC() -- 返回date对象距世界标准时间(UTC)1970年1月1日午夜之间的毫秒数(时间戳)
//     year - 作为date对象的年份，为4位年份值
//     month - 0-11之间的整数，做为date对象的月份
//     day - 1-31之间的整数，做为date对象的天数
//     hours - 0(午夜24点)-23之间的整数，做为date对象的小时数
//     minutes - 0-59之间的整数，做为date对象的分钟数
//     seconds - 0-59之间的整数，做为date对象的秒数
//     microseconds - 0-999之间的整数，做为date对象的毫秒数 */
//     var t1 = Date.UTC(2019, 6, 01, 00, 00, 00); //北京时间2016-12-1 00:00:00
//     var t2 = Date.UTC(todayYear, todayMonth, todayDate, todayHour, todayMinute, todaySecond);
//     var diff = t2 - t1;
//     var diffYears = Math.floor(diff / years);
//     var diffDays = Math.floor((diff / days) - diffYears * 365);
//     var diffHours = Math.floor((diff - (diffYears * 365 + diffDays) * days) / hours);
//     var diffMinutes = Math.floor((diff - (diffYears * 365 + diffDays) * days - diffHours * hours) / minutes);
//     var diffSeconds = Math.floor((diff - (diffYears * 365 + diffDays) * days - diffHours * hours - diffMinutes * minutes) / seconds);
//     document.getElementById("sitetime").innerHTML = "本站已苟活：" + diffYears + " 年 " + diffDays + " 天 " + diffHours + " 小时 " + diffMinutes + " 分钟 " + diffSeconds + " 秒";
// }
// siteTime();