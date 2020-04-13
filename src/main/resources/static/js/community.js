
//登录，模态框跳转注册
// $("#LogToRes").on('click',function(){
//     $("#close-login").trigger("click");
//     $('#myRegister').modal();
// });
function LogToRes(){
    $("#close-login").trigger("click");
    $('#myRegister').modal();
}
//注册，模态框跳转登录
function ResToLog(){
    $("#close-register").trigger("click");
    $('#myLogin').modal();
};





/**
 * 发布问题
 */
function publish() {
    var title=$("#title").val();
    var description=$("#description").val();
    var tag=$("#tag").val();
    var id=$("#id").val();
    if(!title){
        swal("问题标题不能为空~~~", "You clicked the button!", "warning");
        return;
    }
    if(!description){
        swal("问题内容不能为空~~~", "You clicked the button!", "warning");
        return;
    }
    if(!tag){
        swal("问题标签不能为空~~~", "You clicked the button!", "warning");
        return;
    }
    $.ajax({
        type:"POST",
        url:"/publish",
        contentType: 'application/json',
        data:JSON.stringify({
            "title":title,
            "description":description,
            "tag":tag,
            "id":id
        }),
        success:function (response) {
            if(response.code==200){
                swal({
                    title: '发布成功',
                    text: '您已成功发布!',
                    icon: 'success'
                }).then(
                    function () {
                        window.location.href="/";
                    }
                )
            }else{
                if(response.code==2015){
                    swal(response.message, "You clicked the button!", "warning");
                }else if(response.code==2016){
                    swal(response.message, "You clicked the button!", "waring");
                }else if(response.code==2017){
                    swal(response.message, "You clicked the button!", "waring");
                }else if(response.code==2018){
                    swal(response.message, "You clicked the button!", "error");
                }

            }
            console.log(response);
        },
        dataType:"json"
    })
}






/**
 * 提交回复
 */

//回复问题
function post() {
    var questionId=$("#question_id").val();
    var content = $('#comment_content').val();
    comment2Target(questionId,1,content);
}
//回复评论
function comment(e) {
    var commentId=e.getAttribute("data-id");
    var content=$("#input-"+commentId).val();
    comment2Target(commentId,2,content);

}
function comment2Target(targetId,type,content) {
    if(!content){
        //alert("评论不能为空哦~~~");
        swal("评论不能为空哦~~~", "You clicked the button!", "error");
        return;
    }
    $.ajax({
        type:"POST",
        url:"/comment",
        contentType: 'application/json',
        data:JSON.stringify({
            "parentId":targetId,
            "content":content,
            "type":type
        }),
        success:function (response) {
            if(response.code==200){
                //swal("回复成功", "You clicked the button!", "success");
               // window.location.reload();
                swal({
                    title: '回复成功',
                    text: 'You clicked the button!',
                    icon: 'success'
                }).then(
                    function () {
                        window.location.reload();
                    }
                )
            }else{
                if(response.code==2003){
                    swal(response.message, {
                        buttons: ["取消",true],
                    });
                }else{
                    swal(response.message, "You clicked the button!", "error");
                }

            }
            console.log(response);
        },
        dataType:"json"
    })


}

/**
 *
 * 点赞问题
 */
function likeQuestion(e){
    var questionId=e.getAttribute("data-id");
    $.ajax({
        type:"POST",
        url:"/likeQuestion",
        data:{
            "id":questionId
        },
        success:function (response) {
            if(response.code==200){
                swal({
                    title: '点赞成功！',
                    text: '感谢老哥的点赞！',
                    icon: 'success',
                    timer: 2000
                }).then(
                    function () {
                        //var count=eval(response);
                        $("#likeQuestionCount").text(response.data.likeCount);
                        $("#questionlike_btn").css({
                            color:'red',
                        })
                    },
                    function (dismiss) {
                        if (dismiss === 'timer') {
                            console.log('I was closed by the timer')
                        }
                    }
                )
            }else {
                if(response.code==2019){
                    swal({
                        title: response.message,
                        text: response.message,
                        icon: 'warning',
                        timer: 2000
                    }).then(
                        function () {
                        },
                        function (dismiss) {
                            if (dismiss === 'timer') {
                                console.log('I was closed by the timer')
                            }
                        }
                    )

                }
                else if(response.code==2003){
                    swal({
                        title: response.message,
                        text: response.message,
                        icon: 'warning',
                        timer: 2000
                    }).then(
                        function () {
                        },
                        function (dismiss) {
                            if (dismiss === 'timer') {
                                console.log('I was closed by the timer')
                            }
                        }
                    )
                } else if(response.code==2020){
                    swal({
                        title: response.message,
                        text: response.message,
                        icon: 'warning',
                        timer: 2000
                    }).then(
                        function () {
                        },
                        function (dismiss) {
                            if (dismiss === 'timer') {
                                console.log('I was closed by the timer')
                            }
                        }
                    )
                }

            }
            console.log(response);
        },
        dataType:"json"
    })
}

/**
 *点赞评论
 */
function likeComment(e){
    var commentId=e.getAttribute("data-id");
    $.ajax({
        type:"POST",
        url:"/likeComment",
        data:{
            "id":commentId
        },
        success:function (response) {
            if(response.code==200){
                swal({
                    title: '点赞成功！',
                    text: '感谢老哥的点赞！',
                    icon: 'success',
                    timer: 2000
                }).then(
                    function () {
                        //var count=eval(response);
                        $("#likeCommentCount"+commentId).text(response.data.likeCount);
                        $("#commentlike_btn"+commentId).css({
                            color:'red',
                        })
                    },
                    function (dismiss) {
                        if (dismiss === 'timer') {
                            console.log('I was closed by the timer')
                        }
                    }
                )
            }else {
                if(response.code==2019){
                    swal({
                        title: response.message,
                        text: response.message,
                        icon: 'warning',
                        timer: 2000
                    }).then(
                        function () {
                        },
                        function (dismiss) {
                            if (dismiss === 'timer') {
                                console.log('I was closed by the timer')
                            }
                        }
                    )

                }
                else if(response.code==2003){
                    swal({
                        title: response.message,
                        text: response.message,
                        icon: 'warning',
                        timer: 2000
                    }).then(
                        function () {
                        },
                        function (dismiss) {
                            if (dismiss === 'timer') {
                                console.log('I was closed by the timer')
                            }
                        }
                    )
                }else if(response.code==2020){
                    swal({
                        title: response.message,
                        text: response.message,
                        icon: 'warning',
                        timer: 2000
                    }).then(
                        function () {
                        },
                        function (dismiss) {
                            if (dismiss === 'timer') {
                                console.log('I was closed by the timer')
                            }
                        }
                    )
                }

            }
            console.log(response);
        },
        dataType:"json"
    })
}



/*
* 展开二级评论
*
* */
function collapseComments(e) {
    var id=e.getAttribute("data-id");
    var comments=$("#comment-"+ id);
    //获取一下二级评论的展开状态
    var collpase=e.getAttribute("data-collapse");
    if(collpase){
        //折叠二级评论
        comments.removeClass("in");
        e.removeAttribute("data-collapse");
        e.classList.remove("active");
    }else{
        var subCommentContainer=$("#comment-" +id);
        if(subCommentContainer.children().length!=1){
            comments.addClass("in");
            // 标记二级评论展开状态
            e.setAttribute("data-collapse", "in");
            e.classList.add("active");
        }else{
            $.getJSON("/comment/"+id,function (data) {
                console.log(data);
                $.each(data.data.reverse(),function (index,comment) {
                    var mediaLeftElement=$("<div/>",{
                        "class":"media-left"
                    }).append($("<img/>",{
                        "class": "media-object img-rounded",
                        "src": comment.user.avatarUrl
                    }));

                    var mediaBodyElement = $("<div/>", {
                        "class": "media-body"
                    }).append($("<h5/>", {
                        "class": "media-heading",
                        "html": comment.user.name
                    })).append($("<div/>", {
                        "html": comment.content
                    })).append($("<div/>", {
                        "class": "menu"
                    }).append($("<span/>", {
                        "class": "pull-right",
                         "html": moment(comment.gmtCreate).format('YYYY-MM-DD')
                    })));

                    var mediaElement=$("<div/>",{
                        "class":"media"
                    }).append(mediaLeftElement).append(mediaBodyElement);

                    var commentElement=$("<div/>",{
                        "class":"col-log-12 col-md-12 col-sm-12 col-xs-12 comments",
                        // html:comment.content
                    }).append(mediaElement);

                    subCommentContainer.prepend(commentElement);
                });

                //展开二级评论
                comments.addClass("in");
                // 标记二级评论展开状态
                e.setAttribute("data-collapse", "in");
                e.classList.add("active");
            });
        }
    }
}

function showSelectTag() {
    $("#select-tag").show();
}
function selectTag(e) {
    var value = e.getAttribute("data-tag");
    var previous = $("#tag").val();
    if (previous.indexOf(value) == -1) {
        if (previous) {
            $("#tag").val(previous + ',' + value);
        } else {
            $("#tag").val(value);
        }
    }
}
//一键已读
function oneKeyRead() {
    $.ajax({
        type: "GET",
        url: "/oneKeyRead",
        data:{},
        success:function (response) {
            if(response.code==200){
                swal({
                    title: '操作成功',
                    text: '操作成功',
                    icon: 'success',
                    timer: 2000
                }).then(
                    function () {
                        window.location.reload();
                    },
                    function (dismiss) {
                        if (dismiss === 'timer') {
                            console.log('I was closed by the timer')
                        }
                    }
                )
            }
        }
    })

}
function deleteRead() {
    $.ajax({
        type: "GET",
        url: "/deleteRead",
        data:{},
        success:function (response) {
            if(response.code==200){
                swal({
                    title: '操作成功',
                    text: '操作成功',
                    icon: 'success',
                    timer: 2000
                }).then(
                    function () {
                        window.location.reload();
                    },
                    function (dismiss) {
                        if (dismiss === 'timer') {
                            console.log('I was closed by the timer')
                        }
                    }
                )
            }
        }
    })

}
function repeatName() {
    //var username = document.getElementsByName('username')[0].value;
    var username=$("#username").val();
    $.ajax({
        type: "GET",
        url: "/repeatName",
        data:{
            "username":username
        },
        success: function (response) {
            var html="";
            html+="<span>"+response+"</span>";
            $("#msg").html(html);
        }
    })

}
function emailYanZheng() {
    var mail=$("#mail").val();
    var reg = /^([a-zA-Z]|[0-9])(\w|\-)+@[a-zA-Z0-9]+\.([a-zA-Z]{2,4})$/;
    if(!mail){
        sweetAlert(
            '邮箱用户名为空',
            '出错了！',
            'error'
        )
    }else{
        if(reg.test(mail)){
            // swal({
            //     title: '已发送邮件',
            //     text: '发送成功',
            //     icon: 'success',
            //     timer: 2000
            // }).then(
            //     function () {},
            //     // handling the promise rejection
            //     function (dismiss) {
            //         if (dismiss === 'timer') {
            //             console.log('I was closed by the timer')
            //         }
            //     }
            // )
            //开始计时
            var count = 60
            $("#code-btn").attr('disabled','disabled');
            $("#code-btn").html( count + "秒后重新发送");
            var timer = setInterval(function(){
                count--;
                $("#code-btn").html(count + "秒后重新发送");
                if (count==0) {
                    clearInterval(timer);
                    $("#code-btn").attr("disabled",false);//启用按钮
                    $("#code-btn").html("获取验证码");
                    $("#emailYan").html("") ;//清除验证码。如果不清除，过时间后，输入收到的验证码依然有效
                }
            },1000);

            $.ajax({
                type:"GET",
                url:"/emailYanZheng",
                data:{
                    "mail":mail
                },
                success:function (response) {
                    if(response.code==200){
                        swal({
                            title: '已发送邮件',
                            text: '发送成功',
                            icon: 'success',
                            timer: 2000
                        }).then(
                            function () {},
                            // handling the promise rejection
                            function (dismiss) {
                                if (dismiss === 'timer') {
                                    console.log('I was closed by the timer')
                                }
                            }
                        )
                    }
                }
            })
        }else{
            sweetAlert(
                '您输入的不是邮箱！',
                '出错了！',
                'error'
            )
        }
    }

}