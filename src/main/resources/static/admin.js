var socket = new Array();
$(document).ready(function() {
	addUser();
});
function addUser() {
	var userNum = document.getElementById("userNum").value;
	var users = document.getElementById("users");
	var user = document.createElement("tr");
	user.id = userNum;
	users.appendChild(user);
	var thUser = '<th>' + userNum + '</th>';
	var thRoom = '<th><input type="text" width=50 id="roomId@" value="房间号" disabled="true"></th>'
	var thMsg = '<th><input type="text" id="message@" value="消息"></th>'
	var btnConnect = '<input type="button" class="btnConnection" value="加入房间">';
	var btnClose = '<input type="button" id="btnClose@" value="退出房间">';
	var btnMsg = '<input type="button" id="btnSend@" value="发送">';
	var btnCreate = '<input type="button" class="createRoom" value="创建房间">';
	var thOpr = '<th>' + btnConnect + btnClose + btnMsg + btnCreate + '</th>';
	var trNew = thUser + thRoom + thMsg + thOpr;
	trNew = trNew.replace(/@/g, userNum);
	$("#" + userNum).append(trNew);
	// var user = document.createElement("div");
	// user.id = userNum;
	// users.appendChild(user);
	// $("#" + userNum).append(
	// '<input type="text" class="userId" value="用户id" />'
	// + '<input type="text" id="roomId'+userNum+'" value="房间号" />'
	// + '<input type="button" class="btnConnection" value="连接" />'
	// + '<input type="button" id="btnClose'+userNum+'" value="关闭" />'
	// + '<input type="button" id="btnSend'+userNum+'" value="发送" />');
	document.getElementById("userNum").value = userNum - 1 + 2;
	$(".btnConnection").click(
			function() {
				// 实现化WebSocket对象，指定要连接的服务器地址与端口
				var userId = this.parentNode.parentNode.id;
				if (socket[userId] != null) {
					alert("连接已存在！");
					return;
				}
				console.log("roomId" + userId);
				var roomId = document.getElementById("roomId" + userId).value;
				socket[userId] = new WebSocket(
						"ws://localhost:6060/webSocketServer/" + roomId + "-"
								+ userId);
				// 打开事件
				socket[userId].onopen = function() {
					console.log("Socket 已打开");
					// socket.send("这是来自客户端的消息" + location.href + new
					// Date());
				};
				// 获得消息事件
				socket[userId].onmessage = function(msg) {
					console.log(msg.data);
				};
				// 关闭事件
				socket[userId].onclose = function() {
					console.log("Socket已关闭");
				};
				// 发生了错误事件
				socket[userId].onerror = function() {
					console.log("发生了错误");
				}

				// 发送消息
				$("#btnSend" + userId).click(
						function() {
							var msg = document.getElementById("message"+userId).value;
							socket[userId].send("这是来自用户"+userId+"的消息" + msg
									+"，当前时间" +new Date());
						});

				// 关闭
				$("#btnClose" + userId).click(function() {
					socket[userId].close();
					socket[userId]=null;
				});
			});

	$(".createRoom").click(function() {
		// 请求参数
		var userId = this.parentNode.parentNode.id;
		var roomId;
		$.ajax({
			// 请求方式
			type : "POST",
			// 请求的媒体类型
			contentType : "application/json;charset=UTF-8",
			// 请求地址
			url : "room/createRoom?userId=" + userId,
			// 数据，json字符串
			data : {},
			// 请求成功
			success : function(result) {
				roomId = result.roomId;
				document.getElementById("roomId" + userId).value = roomId;
				addRoom(roomId);
			},
			// 请求失败，包含具体的错误信息
			error : function(e) {
				console.log(e.status);
				console.log(e.responseText);
			}
		});
	});
	function addRoom(roomId){
		var rooms = document.getElementById("rooms");
		var room = "<option value="+roomId+">房间"+roomId+"</option>"
		$("#rooms").append(room);
	}
}