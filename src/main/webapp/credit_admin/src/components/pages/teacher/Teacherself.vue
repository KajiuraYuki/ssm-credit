<template>
	<el-container id="one">
		<router-view></router-view>
		<div id="two">
			<!-- 面包屑导航区 -->
			<!-- 面包屑导航区 -->
			<el-breadcrumb separator-class="el-icon-arrow-right">
				<el-breadcrumb-item :to="{ path: '/teacher/welcome' }" @click.native="saveNavState('/teacher/welcome')">首页</el-breadcrumb-item>
				<el-breadcrumb-item>个人中心</el-breadcrumb-item>
				<el-breadcrumb-item>个人信息管理</el-breadcrumb-item>
			</el-breadcrumb>
			<br />
			<el-card>
				<el-form size="small" :model="teacherselfForm" ref="teacherselfForm" :rules="teacherselfFormRules" label-width="35%">
					<el-form-item label="账户">
						<el-input disabled v-model="teacherselfForm.zhanghu" />
					</el-form-item>
					<el-form-item label="姓名" prop="name">
						<el-input v-model="teacherselfForm.name" />
					</el-form-item>
					<el-form-item label="性别" prop="sex">
						<div id="radiosex">
							<el-radio v-model="teacherselfForm.sex" label="男">男</el-radio>
							<el-radio v-model="teacherselfForm.sex" label="女">女</el-radio>
						</div>
					</el-form-item>
					<el-form-item label="所属学院">
						<el-input disabled="disabled" v-model="teacherselfForm.college" />
					</el-form-item>
					<el-form-item label="所属专业">
						<el-input disabled="disabled" v-model="teacherselfForm.major" />
					</el-form-item>
					<el-form-item label="个人邮箱">
						<el-input v-model="teacherselfForm.email" />
					</el-form-item>

					<el-form-item label="个人介绍" prop="content">
						<el-input type="textarea" v-model="teacherselfForm.content" />
					</el-form-item>
				</el-form>
				<el-button type="primary" @click="editteacherSelf">修改个人信息</el-button>
				<el-button type="success" @click="editteacherPassword">修改密码</el-button>
				<el-button type="success" @click="editteacherMibao">修改密保</el-button>
			</el-card>

			<!-- 修改密保对话框 -->
			<el-dialog center title="修改密保" :visible.sync="editMibaoDialogVisible" width="40%" @close="editMbDialogClosed">
				<el-form :model="editMibaoForm" ref="editMibaoForm" :rules="editMibaoFormRules" label-width="35%">
					<el-form-item label="密保问题" prop="wenti">
						<el-input v-model="editMibaoForm.wenti"></el-input>
					</el-form-item>
					<el-form-item label="密保答案" prop="mibao">
						<el-input v-model="editMibaoForm.mibao"></el-input>
					</el-form-item>
				</el-form>
				<span slot="footer" class="dialog-footer">
					<el-button @click="editMibaoDialogVisible = false">取 消</el-button>
					<el-button type="primary" @click="editMibao">确 定</el-button>
				</span>
			</el-dialog>


			<!-- 修改密码对话框 -->
			<el-dialog center title="修改密码" :visible.sync="editPasswordDialogVisible" width="40%" @close="editPwdDialogClosed">
				<el-form :model="editpasswordForm" ref="editpasswordForm" :rules="editpasswordFormRules" label-width="35%">
					<el-form-item label="当前账户">
						<el-input disabled v-model="editpasswordForm.zhanghu"></el-input>
					</el-form-item>
					<el-form-item label="原密码" prop="password0">
						<el-input type="password" v-model="editpasswordForm.password0"></el-input>
					</el-form-item>
					<el-form-item label="新密码" prop="newpassword1">
						<el-input type="password" v-model="editpasswordForm.newpassword1"></el-input>
					</el-form-item>
					<el-form-item label="确认密码" prop="newpassword2">
						<el-input type="password" v-model="editpasswordForm.newpassword2"></el-input>
					</el-form-item>
				</el-form>
				<span slot="footer" class="dialog-footer">
					<el-button @click="editPasswordDialogVisible = false">取 消</el-button>
					<el-button type="primary" @click="editPassWord">确 定</el-button>
				</span>
			</el-dialog>
		</div>
	</el-container>
</template>

<script>
	export default {
		name: 'teacherSelf',
		data() {

			var passworCheck = (rule, value, callback) => {
				if (value !== window.sessionStorage.getItem('pwd').trim()) {
					callback(new Error('原密码输入不正确!请重新输入!'))
				} else {
					callback()
				}
			}
			var newpasswordCheck = (rule, value, callback) => {
				if (value !== this.editpasswordForm.newpassword1.trim()) {
					callback(new Error('两次密码输入不一致！请重新输入！'))
				}
				if (value === this.teacherselfForm.password.trim()) {
					callback(new Error("新密码不能与原密码一样！请重新输入!"))
				} else {
					callback()
				}
			}


			return {
				teacherselfForm: {},
				teachername: '',
				teachersex: '',
				teachercontent: '',
				teacheremail: '',
				teacherwenti: '',
				teachermibao: '',
				editMibaoDialogVisible: false,
				editMibaoForm: {
					wenti: '',
					mibao: '',
				},
				editMibaoFormRules: {
					mibao: [{
							required: true,
							message: '密保答案不能为空!',
							trigger: 'change'
						},
						{
							min: 6,
							message: '密保答案长度至少为6位！'
						},
					],
					wenti: [{
							required: true,
							message: '密保问题不能为空!',
							trigger: 'change'
						},
						{
							min: 4,
							message: '密保问题长度至少为4位！'
						},
					]
				},
				teacherselfFormRules: {
					name: [{
						required: true,
						message: '姓名不能为空!',
						trigger: 'change'
					}]
				},
				editPasswordDialogVisible: false,
				editpasswordForm: {},
				editpasswordFormRules: {
					password0: [{
							required: true,
							message: '原密码不能为空!',
							trigger: 'change'
						},
						{
							min: 6,
							max: 16,
							message: '密码长度至少为6位！'
						},
						{
							validator: passworCheck
						}
					],
					newpassword1: [{
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
					newpassword2: [{
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
		created() {
			this.saveNavState('/teacher/self')
			this.getteacherSelf()
		},
		methods: {
			// 获取当前账户所有信息
			getteacherSelf() {
				this.$axios.get('teacher/getteacherself/' + window.sessionStorage.getItem('username'))
					.then(res => {
						if (res.status !== 200) {
							return this.$message.error("获取当前用户信息失败!")
						}
						this.teacherselfForm = res.data
						this.teachername = this.teacherselfForm.name
						this.teachersex = this.teacherselfForm.sex
						this.teacherwenti = this.teacherselfForm.wenti
						this.teachermibao = this.teacherselfForm.mibao
						this.teachercontent = this.teacherselfForm.content
						this.teacheremal = this.teacherselfForm.email
					})
			},
			// 修改当前账户基本信息
			editteacherSelf() {
				if (this.teacherselfForm.email == this.teacheremal &&
					this.teacherselfForm.name === this.teachername &&
					this.teacherselfForm.sex === this.teachersex &&
					this.teacherselfForm.content === this.teachercontent) {
					return this.$message.error("数据未改变,无须更新数据!")
				}

				this.$refs.teacherselfForm.validate(async valid => {
					if (valid) {
						this.$axios.put("teacher/updateself/" + window.sessionStorage.getItem("username"), this.teacherselfForm)
							.then(res => {
								if (res.status !== 200) {
									return this.$message.error("更新用户信息失败！")
								}
								this.teacherselfForm = res.data
								this.$message.success("更新用户信息成功!")
								this.getteacherSelf()
							})
					}
				})
			},
			// 修改密保按钮事件
			editteacherMibao() {
				this.editMibaoDialogVisible = true
				this.editMibaoForm.wenti = this.teacherselfForm.wenti
				this.editMibaoForm.mibao = this.teacherselfForm.mibao
			},
			// 修改密码按钮事件
			editteacherPassword() {
				this.editPasswordDialogVisible = true
				this.editpasswordForm.zhanghu = this.teacherselfForm.zhanghu
			},
			// 修改当前账户密码对话框中的确定事件
			editPassWord() {
				this.$refs.editpasswordForm.validate(async valid => {
					if (valid) {
						this.teacherselfForm.password = this.editpasswordForm.newpassword2
						this.$axios.put("teacher/updateselfpwd/" + window.sessionStorage.getItem("username"), this.teacherselfForm)
							.then(res => {
								if (res.status !== 200) {
									return this.$message.error("更新密码失败！请稍候重试！")
								}
								this.$message.success("更新密码成功！请重新进行登录！")
								// 执行退出操作
								window.sessionStorage.clear()
								this.$router.push('/')
							})
					}
				})
			},
			// 修改当前账户密保对话框中的确定事件
			editMibao() {
				// 当前操作并未修改密保
				if (this.editMibaoForm.wenti == this.teacherwenti &&
					this.editMibaoForm.mibao == this.teachermibao) {
					this.editMibaoDialogVisible = false
					return this.$message.error("数据未改变，无需更改数据！")
				}
				this.$refs.editMibaoForm.validate(async valid => {
					if (valid) {
						this.teacherselfForm.wenti = this.editMibaoForm.wenti
						this.teacherselfForm.mibao = this.editMibaoForm.mibao
						this.$axios.put('teacher/updateself/' + window.sessionStorage.getItem("username"), this.teacherselfForm)
							.then(res => {
								if (res.status !== 200) {
									return this.$message.error("更新密保信息失败！")
								}
								this.teacherselfForm = res.data
								this.teacherwenti = this.teacherselfForm.wenti
								this.teachermibao = this.teacherselfForm.mibao
								this.$message.success("更新密保信息成功!")
								this.editMibaoDialogVisible = false
							})
					}
				})
			},
			// 监听 修改密保 对话框关闭事件
			editMbDialogClosed() {
				this.$refs['editMibaoForm'].resetFields();
			},
			// 监听 修改密码 对话框关闭事件
			editPwdDialogClosed() {
				this.$refs['editpasswordForm'].resetFields();
			},
			// 保存链接的激活地址
			saveNavState(activePath) {
				this.resetSetItem('activePath', activePath)
				// window.sessionStorage.setItem('activePath',activePath)					
			}

		},
	}
</script>

<style scoped="scoped">
	#one {
		width: 100%;
		height: auto;
	}

	#two {
		width: 100%;
		height: 100%;
	}

	.el-card {
		height: 100%;
		width: 100%;
		text-align: center;
	}

	.el-input,
	.el-textarea {
		width: 50%;
		display: flex;
	}

	.el-radio {
		float: left;
		margin-top: 13px;
	}
</style>
