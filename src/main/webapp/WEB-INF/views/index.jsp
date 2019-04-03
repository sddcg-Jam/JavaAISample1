<%--
  ~ Copyright (c) 博学谷 2019.
  ~ Author：Jam Fang
  ~ https://www.jianshu.com/u/0977ede560d4
  ~
  --%>

<!DOCTYPE html>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html class="no-js">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="description" content="Java+人工智能实战案例">
    <meta name="keywords" content="Java+人工智能实战案例">
    <meta name="viewport"
          content="width=device-width, initial-scale=1">
    <title>Java老鸟福利：Java+人工智能来了</title>
    <!-- Set render engine for 360 browser -->
    <meta name="renderer" content="webkit">
    <!-- No Baidu Siteapp-->
    <meta http-equiv="Cache-Control" content="no-siteapp"/>
    <meta name="mobile-web-app-capable" content="yes">

    <!-- Add to homescreen for Safari on iOS -->
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">
    <meta name="apple-mobile-web-app-title" content="Amaze UI"/>

    <!-- Tile icon for Win8 (144x144 + tile color) -->
    <meta name="msapplication-TileColor" content="#0e90d2">

    <link rel="stylesheet" href="//cdn.amazeui.org/amazeui/2.7.2/css/amazeui.min.css">
    <script src="${pageContext.request.contextPath}/js/jquery-3.3.1.js" type="text/javascript"></script>
    <!--[if lt IE 9]>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/flashcanvas.js"></script>
    <![endif]-->
    <style type="text/css">
        #signatureparent {
            position: relative;
        }

        #signature {
            border: 1px solid green;
            position: absolute;
            margin-left: 50%;
            left: -28px;
            margin-top: 10px;
        }

        .btn {
            margin-top: 280px;
            text-align: center;
        }
    </style>
</head>
<body>

<div claas=".am-container .am-center">
    <h1> 博学谷进阶公开课：Java老司机福利-Java+人工智能来了</h1>
    <div class="am-panel-group" id="accordion">
        <div class="am-panel am-panel-primary">
            <div class="am-panel-hd">
                <h3 class="am-panel-title" data-am-collapse="{parent: '#accordion', target: '#do-not-say-1'}">课程目标：</h3>
            </div>
            <div id="do-not-say-1" class="am-panel-collapse am-collapse am-in">
                <div class="am-panel-bd">
                    <ul>
                        <li>1、通过Java实现BP神经网络算法</li>
                        <li>2、通过MNIST对模型进行训练，测试集准确率达到95%以上</li>
                        <li>3、通过调用训练好的模型进行手写数字识别</li>
                    </ul>
                </div>
            </div>
            <div class="am-panel am-panel-primary">
                <div class="am-panel-hd">
                    <h3 class="am-panel-title" data-am-collapse="{parent: '#accordion', target: '#do-not-say-2'}">
                        演示训练模型：</h3>
                </div>

                <div id="do-not-say-2" class="am-panel-collapse am-collapse">
                    <div class="am-panel-bd">

                        <div style="text-align: center;">
                            <button type="button" class="am-btn am-btn-primary btn-loading-train"
                                    data-am-loading="{spinner: 'circle-o-notch', loadingText: '训练中...', resetText: '开始训练'}">
                                开始训练
                            </button>
                        </div>
                        训练进度展示：
                        <div class="am-progress">
                            <div id="processBar" class="am-progress-bar am-progress-bar-success" style="width: 0%;">0%
                            </div>
                        </div>
                        <div id="processBarDes">请点击开始训练</div>
                    </div>
                </div>
            </div>
            <div class="am-panel am-panel-primary">
                <div class="am-panel-hd">
                    <h3 class="am-panel-title" data-am-collapse="{parent: '#accordion', target: '#do-not-say-3'}">
                        演示模型识别手写字体：</h3>
                </div>
                <div id="do-not-say-3" class="am-panel-collapse am-collapse  am-in">
                    <div class="am-panel-bd ">
                        手写板区域
                        图片上传至服务端，服务端进行识别，返回结果。
                        <div id="signature"></div>
                        <div class="btn">
                            <button type="button" class="am-btn am-btn-primary btn-loading-example"
                                    data-am-loading="{spinner: 'circle-o-notch', loadingText: '重置中...', resetText: '重置画板'}">
                                重置画板
                            </button>

                            <input type="button" class="am-btn am-btn-secondary btn-loading-submit" value="识别数字"
                                   data-am-loading="{loadingText: '模型识别中...'}"/>
                            <input type="button" class="am-btn am-btn-secondary btn-loading-submit-withDL4J"
                                   value="使用DL4J识别"
                                   data-am-loading="{loadingText: '模型识别中...'}"/>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

</div>
<div class="am-modal am-modal-alert" tabindex="-1" id="my-alert">
    <div class="am-modal-dialog">
        <div class="am-modal-hd">提示信息</div>
        <div class="am-modal-bd" id="test-result">
            识别成功
        </div>
        <div class="am-modal-footer">
            <span class="am-modal-btn">确定</span>
        </div>
    </div>
</div>

<script src="${pageContext.request.contextPath}/js/jSignature.min.js" type="text/javascript"></script>
<script src="http://cdn.amazeui.org/amazeui/2.7.2/js/amazeui.min.js"></script>
<script>
    var $signature;
    $(document).ready(function () {
        //首先初始化jSignature：
        $signature = $("#signature").jSignature({
            'height': 240,
            'width': 240,
            'decor-color': 'transparent',
            'lineWidth': 13
        });
    });

    //开始训练模型
    $('.btn-loading-train').click(function () {
        var $btn = $(this)
        $btn.button('loading');
        //调用服务接口开始训练模型
        $.get("/train");
        //每隔500ms请求服务器获取训练进度
        var processBar = $('#processBar');
        var processBarDes = $('#processBarDes');
        processBarDes.html("训练已经开始。。。")
        var t = setInterval(function working() {
            $.getJSON("/processBar", {}, function (result) {
                var percent = (result.currentStep * 100 / result.totalStep).toFixed(2) + "%";
                console.log(percent)
                processBar.html(percent);
                processBar.css("width", percent);
                processBarDes.html("正在进行第" + result.currentStage + "阶段:[" + result.name + "]，共" + result.totalStage + "个阶段");
                if (result.currentStage >= result.totalStage && result.currentStep >= result.totalStep) {
                    processBar.html("100%");
                    processBar.css("width", "100%");
                    processBarDes.html("模型训练结束，测试准确率为:" + result.name);
                    clearInterval(t);
                }
                console.log(result);
            });
        }, 500);

        setTimeout(function () {
            $btn.button('reset');
        }, 500);
    });
    //重置画板
    $('.btn-loading-example').click(function () {
        var $btn = $(this)
        $btn.button('loading');
        //（1）、重置画布
        $signature.jSignature('reset');
        setTimeout(function () {
            $btn.button('reset');
        }, 500);
    });

    //提交服务器识别
    $('.btn-loading-submit').click(function () {
        var $btn = $(this)
        $btn.button('loading');
        postData("/recognition");
        setTimeout(function () {
            $btn.button('reset');
        }, 500);
    });

    //提交服务器用DL4J进行识别
    $('.btn-loading-submit-withDL4J').click(function () {
        var $btn = $(this)
        $btn.button('loading');
        postData("/recognitionWithDL4j");
        setTimeout(function () {
            $btn.button('reset');
        }, 500);
    });

    //（2）、获取数据提交到服务器端
    function postData(url) {
        //获取数据
        var data = $signature.jSignature('getData', 'image');//'svgbase64');
        // 使用formdata上传图片 data[1]是png图片的base64
        var f = new FormData();
        f.append("fileStr", data[1]);
        $.post(url, {'fileStr': data[1]}, function (result) {
            $("#test-result").html("识别结果：" + result);
            $('#my-alert').modal();
        });
    }
</script>
</body>
</html>