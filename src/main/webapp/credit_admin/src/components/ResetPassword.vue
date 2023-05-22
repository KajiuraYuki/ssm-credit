<template>
	<div id="poster">
		<el-form :model="ResetPwdForm" ref="ResetPwdForm" :rules="ResetPwdFormRules" class="forgete-container" label-position="left"
		 label-width="0px">
			<h2 class="forgete_title">重置密码</h2>
			<el-form-item prop="password">
				<el-input clearable prefix-icon="iconfont icon-denglu-mima" type="password" v-model="ResetPwdForm.password"
				 auto-complete="off" placeholder="新密码"></el-input>
			</el-form-item>

			<el-form-item prop="newpassword">
				<el-input clearable prefix-icon="iconfont icon-denglu-mima" type="password" v-model="ResetPwdForm.newpassword"
				 auto-complete="off" placeholder="确认密码"></el-input>
			</el-form-item>

			<el-form-item style="text-align: center;">
				<el-button type="info" v-on:click="ResetPwd">重置密码</el-button>
			</el-form-item>
		</el-form>

	</div>
</template>

<script>
	let Base64 = require('js-base64').Base64
	export default {
		data() {
			var newpasswordCheck = (rule, value, callback) => {
				if (value !== this.ResetPwdForm.password.trim()) {
					callback(new Error('两次密码输入不一致！请重新输入！'))
				} else {
					callback()
				}
			}
			return {
				zhanghu: '',
				typevalue: '',
				code: '',
				ResetPwdForm: {
					password: '',
					newpassword: ''
				},
				ResetPwdFormRules: {
					password: [{
							required: true,
							message: '新密码不能为空!',
							trigger: 'change'
						},
						{
							min: 6,
							max: 16,
							message: '密码长度至少为6位！'
						}
					],
					newpassword: [{
							required: true,
							message: '确认密码不能为空!',
							trigger: 'change'
						},
						{
							min: 6,
							max: 16,
							message: '密码长度至少为6位！'
						},
						{
							validator: newpasswordCheck
						}
					]
				}
			}
		},
		/**
		 * 监听url变化，当url改变的时候刷新页面，重新对url进行判断
		 * **/
		watch: {
			'$route'(to, from) {
				location.reload()
			}
		},
		mounted: function() {
			/**
			 * 对url地址进行判断，首先看是否满足四个参数都有
			 * **/
			var urlStr = window.location.href.toString().split('?')[1]
			// 将url ? 后面的字符串进行解码
			urlStr = Base64.decode(urlStr)
			// console.log(urlStr)
			// 对 urlStr进行处理
			var arrUrlStr = urlStr.split('&')
			for (var i = 0; i < arrUrlStr.length; i++) {
				arrUrlStr[i] = arrUrlStr[i].toString().split('=')[1]
			}
			if (arrUrlStr[0] == null || arrUrlStr[1] == null || arrUrlStr[2] == null) {
				return this.openMsg('url有误！请输入正确的url地址！')
			}
			/**
			 * 根据url中的四个参数与数据库进行比对，当有记录的时候就验证通过，可以进行重置密码
			 * **/
			switch (arrUrlStr[0]) {
				case 'admin':
					arrUrlStr[0] = 1
					break
				case 'teacher':
					arrUrlStr[0] = 2
					break
				case 'student':
					arrUrlStr[0] = 3
			}
			this.$axios.post('resetpwd/checkstatus', {
				typevalue: arrUrlStr[0],
				zhanghu: arrUrlStr[1],
				code: arrUrlStr[2]
			}).then(res => {
				if (res.data.code === '255' || res.data.code === '256') {
					return this.openMsg(res.data.msg)
				} 
				this.zhanghu = arrUrlStr[1]
				this.typevalue = arrUrlStr[0]
				this.code = arrUrlStr[2]
			})


			// console.log(arrUrlStr[0], arrUrlStr[1], arrUrlStr[2])
		},
		methods: {
			/**
			 * 消息提示函数  传递提示的消息  显示一个不可关闭的msgbox
			 * **/
			openMsg(returnmsg) {
				this.$alert('<p style="color:red;font-size:20px;">' + returnmsg + '</p>', '', {
					dangerouslyUseHTMLString: true,
					showClose: false,
					showConfirmButton: false,
					center: true
				});
			},
			/**
			 * 重置密码
			 * **/
			ResetPwd() {
				this.$refs.ResetPwdForm.validate(async valid => {
					if (valid) {
						// 账户类型为 student
						console.log(this.typevalue)
						if (this.typevalue === 3) {
							this.$axios.post('student/resetpwd', {
									password: this.ResetPwdForm.newpassword,
									zhanghu: this.zhanghu
								})
								.then(res => {
									if (res.status !== 200) {
										return this.$message.error("向服务器请求数据失败！请稍候再试！")
									}
									if (res.data.code === '255') {
										return this.$message.error(res.data.msg)
									}
									if (res.data.code === '200') {
										this.$router.push('/')
										return this.$message.success(res.data.msg)
									}
								})
						}
						/**
						 * 账户类型为 admin
						 * **/
						if (this.typevalue === 1) {
							this.$axios.post('admin/resetpwd', {
									password: this.ResetPwdForm.newpassword,
									zhanghu: this.zhanghu
								})
								.then(res => {
									if (res.status !== 200) {
										return this.$message.error("向服务器请求数据失败！请稍候再试！")
									}
									if (res.data.code === '255') {
										return this.$message.error(res.data.msg)
									}
									if (res.data.code === '200') {
										this.$router.push('/')
										return this.$message.success(res.data.msg)
									}
								})
						}
						/**
						 * 账户类型为 teacher
						 * **/
						if (this.typevalue === 2) {
							this.$axios.post('teacher/resetpwd', {
									password: this.ResetPwdForm.newpassword,
									zhanghu: this.zhanghu
								})
								.then(res => {
									if (res.status !== 200) {
										return this.$message.error("向服务器请求数据失败！请稍候再试！")
									}
									if (res.data.code === '255') {
										return this.$message.error(res.data.msg)
									}
									if (res.data.code === '200') {
										this.$router.push('/')
										return this.$message.success(res.data.msg)
									}
								})
						}
					}
				})
			}
		}
	}
</script>

<style scoped="scoped">
	#poster {
		background: url(../assets/images/bj.png);
		background-position: center;
		height: 100%;
		width: 100%;
		background-size: cover;
		position: fixed;

	}

	.forgete-container {
		border-radius: 15px;
		background-clip: padding-box;
		margin: 10% auto;
		width: 25%;
		padding: 35px 35px 15px 35px;
		background: #d8e3e7;
		border: 1px solid #eaeaea;
		box-shadow: 0 0 25px #cac6c6;
	}

	.forgete_title {
		margin: 0px auto 40px auto;
		text-align: center;
		color: #505458;
	}
</style>
