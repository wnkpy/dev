<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html>
<html>
<head>
  <style>
    .m1{
      margin-left: 10px;
    }
    .s1{
      font-weight: bold;
      color:#1e9fff;
    }
    .m2{
      margin-bottom: 10px;
    }
  </style>
</head>
<body  class="easyui-layout">

    <h1 class="m1">SpringBoot导出word：</h1>
    <div class="easyui-tabs" style="width:700px;height:250px;margin: 10px 0 0 10px;">
        <div title="" style="padding:10px">
	        <div class="m2">使用<span class="s1">POI-tl</span>根据word模板动态生成word</div>
			<a href="#" class="easyui-linkbutton" onclick="doExportWord1();" data-options="iconCls:'icon-save'">下载word</a>
        </div>
    </div>
	<script type="text/javascript">
	  //方式一导出word
	  function doExportWord1(){
	    window.location.href="<%=basePath%>/auth/exportWord/exportUserWord";
	  }
	</script>
</body>
</html>



