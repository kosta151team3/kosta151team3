<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<script src="https://code.jquery.com/jquery-2.2.4.min.js"></script>
	<script type="text/javascript">
		$(document).ready(function(){
			/* $('#ajaxBtn').click(function(){
				 var array = new Array();
				     array[0] = "a";
				     array[1] = "b";
				 $.ajax(
						 {
							type : "post",
							url  : "json.kosta",
							data : "command=AjaxTest&name=java&arr="+array,
							success : function(data){
								console.log(data);
								$('#menuView').empty();
								var opr="";
								$.each(data.menu,function(index,value){
									console.log(index + "/" + value);
									opr += index + "." + value + "<br>";
								});
								$('#menuView').append(opr);
							} 
						 } 
				       )    
			}); */
			
			
			$('#submit').click(function(){
				var sendpwd = $('#pwd').val();
				console.log("111111");
				console.log(sendpwd);
				 $.ajax(
						 {
							type : "post",
							url  : "memberConfirm.htm",
							data : "pwd="+sendpwd,
							success : function(data){
								if (data==null) {
									$('#content').prepend("올바른 비밀번호가 아닙니다");
								}else{
									console.log(data);
									$('#content').empty();
									var opr="";
									/* $.each(data.menu,function(index,obj){
										console.log(index + "/" + obj);
										opr += obj.beer + "<br>";
										opr += obj.food + "<br>";
										opr += index +"<br><hr>" 
									}); */
									
									$('#content').append(data.name);
								}
								
							} 
						 } 
				       )    
			});
			
			
			/* $('#ajaxBtn3').click(function(){
				 $.ajax(
						 {
							type : "post",
							url  : "json3.kosta",
							success : function(data){
								console.log(data);
								$('#menuView').empty();
								var opr="";
								$.each(data.data,function(index,obj){
									console.log(index + "/" + obj);
									opr += obj.firstname + "<br>";
									opr += obj.lastname + "<br>";
									opr += obj.email + "<br>";
									opr += index +"<br><hr>"
								});
								$('#menuView').append(opr);
							} 
						 } 
				       )    
			});
			 */
			
			/* $('#ajaxBtn4').click(function(){
				var aaa = [];
				bbb = "NEWDATA";
				ccc = '[{"Product" : "Mouse", "Maker":"Samsung", "Price":23000},{"Product" : "KeyBoard", "Maker":"LG", "Price":12000}]';
				aaa.push(10);
				aaa.push(20);
				aaa.push(30);
				aaa.push(40);


				 $.ajax(
						 {
							type : "post",
							url  : "json4.kosta",
							data : {aaa:aaa, bbb:bbb, ccc:ccc}, //?aaa=aaa&bbb=bbb&
							success : function(data){
								console.log(data);
							} 
						 } 
				       )    
			}); */
		});
	
</script>	
	
<div id=content>
	<!-- <form method="POST"> -->
		비밀번호를 입력해주세요 : <input type="password" name="pwd" id="pwd"> 
		<input id="submit" type="submit" value="확인">
	<!-- </form> -->
</div>
