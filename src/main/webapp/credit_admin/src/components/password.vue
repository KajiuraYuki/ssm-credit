<template>
	<div id="poster">
		<el-form :model="forgeteForm" ref="forgeteForm" :rules="forgeteFormRules" class="forgete-container" label-position="left"
		 label-width="0px">
			<h2 class="forgete_title">忘记密码</h2>
			<el-form-item prop="typevalue">
				<el-select style="width: 100%;" v-model="forgeteForm.typevalue" placeholder="请选择账户类型">
					<el-option v-for="item in options" :key="item.typevalue" :label="item.label" :value="item.typevalue"></el-option>
				</el-select>
			</el-form-item>
			<el-form-item prop="zhanghu">
				<el-input clearable prefix-icon="iconfont icon-denglu" type="text" v-model="forgeteForm.zhanghu" auto-complete="off"
				 placeholder="账号"></el-input>
			</el-form-item>

			<el-form-item prop="email">
				<el-input clearable prefix-icon="iconfont icon-youxiang" type="email" v-model="forgeteForm.email" auto-complete="off"
				 placeholder="邮箱"></el-input>
			</el-form-item>
			
			<!-- 输入密保，默认隐藏，通过前面的验证之后才可进行密保验证 -->
			<el-form-item prop="mibao">
				<el-input clearable prefix-icon="iconfont icon-denglu-mima" type="text" v-model="forgeteForm.mibao" auto-complete="off"
				 placeholder="密保" ></el-input>
			</el-form-item>

			<el-form-item style="text-align: center;">
				<el-button type="info" v-on:click="Retrieve">找回密码</el-button>
			</el-form-item>
		</el-form>
	</div>
</template>

<script>
	export default {
		data() {
			// 自定义邮箱规则
			var checkEmail = (rule, value, callback) => {
				const regEmail = /^\w+@\w+(\.\w+)+$/
				if (regEmail.test(value)) {
					// 合法邮箱
					return callback()
				}
				callback(new Error('请输入合法邮箱'))
			}
			return {
				forgeteForm: {
					zhanghu: '',
					email: '',
					typevalue: '',
					mibao: ''
				},
				options: [{
					typevalue: 'admin',
					label: '管理员账户',
				}, {
					typevalue: 'teacher',
					label: '教师账户',
				}, {
					typevalue: 'student',
					label: '学生账户',
				}],
				// 表单验证
				forgeteFormRules: {
					// 下拉框验证
					typevalue: [{
						required: true,
						message: '请选择登录类型！',
						trigger: 'change'
					}],
					zhanghu: [{
						required: true,
						message: '账号不能为空！',
						trigger: 'change'
					}],
					email: [{
							required: true,
							message: '邮箱不能为空！',
							trigger: 'change'
						},
						{
							validator: checkEmail
						}
					],
					mibao: [{
						required: true,
						message: '密保不能为空！',
						trigger: 'change'
					}]
				}
			}
		},
		methods: {
			// 找回密码操作
			async Retrieve() {
				// 验证表单
				console.log(this.forgeteForm)
				this.$refs.forgeteForm.validate(async valid => {
					if (valid) {
						// 账户类型为管理员
						if (this.forgeteForm.typevalue === 'admin') {
							this.$axios.post('/admin/forgetepwd', {
								email: this.forgeteForm.email,
								zhanghao: this.forgeteForm.zhanghu,
								mibao: this.forgeteForm.mibao
							}).then(res => {
								if (res.data.code === '255' || res.data.code === '256') {
									return this.$message.error(res.data.msg)
								}
								if (res.data.code === '200') {
									this.$message.success(res.data.msg + '  请及时查看！')
									this.$router.push('/')
								}
							})
						}
						// 账户类型为教师
						if (this.forgeteForm.typevalue === 'teacher') {
							this.$axios.post('/teacher/forgetepwd', {
								email: this.forgeteForm.email,
								zhanghao: this.forgeteForm.zhanghu,
								mibao: this.forgeteForm.mibao
							}).then(res => {
								if (res.data.code === '255' || res.data.code === '256') {
									return this.$message.error(res.data.msg)
								}
								if (res.data.code === '200') {
									this.$message.success(res.data.msg + '  请及时查看！')
									this.$router.push('/')
								}
							})
						}
                        // 账户类型为学生
							if (this.forgeteForm.typevalue === 'student') {
								this.$axios.post('/student/forgetepwd', {
									email: this.forgeteForm.email,
									zhanghao: this.forgeteForm.zhanghu,
									mibao: this.forgeteForm.mibao
								}).then(res => {
									if (res.data.code === '255' || res.data.code === '256') {
										return this.$message.error(res.data.msg)
									}
									if (res.data.code === '200') {
										this.$message.success(res.data.msg + '  请及时查看！')
										this.$router.push('/')
									}
								})
							}
					}
				})
			}
		}
	}
</script>

<style scoped>
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
