<template>
	<el-container id="one">
		<router-view></router-view>
		<div id="two">
			<!-- 面包屑导航区 -->
			<!-- 面包屑导航区 -->
			<el-breadcrumb separator-class="el-icon-arrow-right">
			  <el-breadcrumb-item :to="{ path: '/student/welcome' }" @click.native="saveNavState('/student/welcome')">首页</el-breadcrumb-item>
			  <el-breadcrumb-item>个人中心</el-breadcrumb-item>
			  <el-breadcrumb-item>个人信息管理</el-breadcrumb-item>
			</el-breadcrumb>
			<br />
			<el-card>
				<el-form
					size="mini"
					:model="studentselfForm"
					ref="studentselfForm"
					:rules="studentselfFormRules"
					label-width="35%">
					<el-form-item label="学号">
						<el-input disabled v-model="studentselfForm.xuehao"></el-input>
					</el-form-item>
					<el-form-item label="姓名" prop="name">
						<el-input v-model="studentselfForm.name" />
					</el-form-item>
					<el-form-item label="性别" prop="sex">
						<div id="radiosex">
							<el-radio v-model="studentselfForm.sex" label="男">男</el-radio>
							<el-radio v-model="studentselfForm.sex" label="女">女</el-radio>						
						</div>						
					</el-form-item>
					<el-form-item label="所属学院">
						<el-input disabled="disabled" v-model="studentselfForm.college" />
					</el-form-item>
					<el-form-item label="所属专业">
						<el-input disabled="disabled" v-model="studentselfForm.major" />
					</el-form-item>
					<el-form-item label="所在班级">
						<el-input disabled="disabled" v-model="studentselfForm.banji" />
					</el-form-item>
					<el-form-item label="个人邮箱">
						<el-input v-model="studentselfForm.email" />
					</el-form-item>					
					<el-form-item label="个人介绍" prop="content">
						<el-input type="textarea" v-model="studentselfForm.content" />
					</el-form-item>
				</el-form>
				<el-button size="small" type="primary" @click="editstudentSelf">修改个人信息</el-button>
				<el-button size="small" type="success" @click="editstudentPassword">修改密码</el-button>
				<el-button size="small" type="success" @click="editstudentMibao">修改密保</el-button>
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
				<el-form
					:model="editpasswordForm"
					ref="editpasswordForm"
					:rules="editpasswordFormRules"
					label-width="35%">
					<el-form-item label="当前账户">
						<el-input disabled v-model="editpasswordForm.xuehao"></el-input>
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
		name: 'StudentSelf',
		data() {
			
			var passworCheck = (rule, value,callback) => {
				if (value !== window.sessionStorage.getItem('pwd').trim()) {
					callback(new Error('原密码输入不正确!请重新输入!'))
				}
				else {
					callback()
				}
			}
			var newpasswordCheck = (rule, value, callback) => {
				if (value !== this.editpasswordForm.newpassword1.trim()) {
					callback(new Error('两次密码输入不一致！请重新输入！'))
				}
				if (value === this.studentselfForm.password.trim()) {
					callback(new Error("新密码不能与原密码一样！请重新输入!"))
				}
				else {
					callback()
				}
			}
			
			return {
				studentselfForm: {},
				studentname: '',
				studentsex: '',
				studentwenti: '',
				studentmibao: '',
				studetnemail: '',
				studentcontent: '',
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
				studentselfFormRules: {
					name: [
						{ required: true, message: '姓名不能为空!' ,trigger: 'change' }
					]
				},
				editPasswordDialogVisible: false,
				editpasswordForm: {},
				editpasswordFormRules: {
					password0: [
						{ required: true,message: '原密码不能为空!', trigger: 'change' },
						{ min: 6 , max: 16 , message: '密码长度至少为6位！'},
						{ validator: passworCheck }
					],
					newpassword1: [
						{ required: true,message: '新密码不能为空!', trigger: 'change' },
						{ min: 6 , max: 16 , message: '密码长度至少为6位！' }
					],
					newpassword2: [
						{ required: true,message: '确认密码不能为空!', trigger: 'change' },
						{ min: 6 , max: 16 , message: '密码长度至少为6位！'},
						{ validator: newpasswordCheck }
					]
				}
			}
		},
		created() {
			this.saveNavState('/student/self')
			this.getstudentSelf()
		},
		methods: {
			// 获取当前账号的基本信息
			getstudentSelf() {
				this.$axios.get('student/getself/' + window.sessionStorage.getItem("username"),this.studentselfForm)
				.then(res => {
					if (res.status !== 200) {
						return this.$message.error('获取当前账户信息失败！')
					}
					this.studentselfForm = res.data
					this.studentname = this.studentselfForm.name
					this.studentsex = this.studentselfForm.sex
					this.studentwenti = this.studentselfForm.wenti
					this.studentmibao = this.studentselfForm.mibao
					this.studetnemail = this.studentselfForm.email
					this.studentcontent = this.studentselfForm.content
				})
			},
			// 对当前账户的基本信息(可修改部分)进行修改
			editstudentSelf() {
				if (this.studentselfForm.name == this.studentname && this.studentselfForm.sex == this.studentsex && this.studentselfForm.email == this.studetnemail && this.studentselfForm.content == this.studentcontent) {
					return this.$message.error("数据未改变，无需更改数据！")
				}
				this.$refs.studentselfForm.validate(async valid => {
					if(valid) {
						this.$axios.put('student/updateself/' + window.sessionStorage.getItem("username"),this.studentselfForm)
						.then(res => {
							if (res.status !== 200) {
								return this.$message.error("更新用户数据失败！")
							}
							this.studentselfForm = res.data
							this.$message.success("更新用户信息成功!")
							this.getstudentSelf()
						})
					}
				})
			},
			// 修改密码按钮事件
			editstudentPassword() {
				this.editPasswordDialogVisible = true
				this.editpasswordForm.xuehao = this.studentselfForm.xuehao
			},
			// 修改密保按钮事件
			editstudentMibao() {
				this.editMibaoDialogVisible = true
				this.editMibaoForm.wenti = this.studentselfForm.wenti
				this.editMibaoForm.mibao = this.studentselfForm.mibao
			},
			// 修改当前账户密码对话框中的确定事件
			editPassWord () {
				this.$refs.editpasswordForm.validate(async valid => {
					if (valid) {						
						this.studentselfForm.password = this.editpasswordForm.newpassword2
						this.$axios.put("student/updateselfpwd/" + window.sessionStorage.getItem("username"),this.studentselfForm)
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
				if (this.editMibaoForm.wenti == this.studentwenti &&
					this.editMibaoForm.mibao == this.studentmibao) {
					this.editMibaoDialogVisible = false
					return this.$message.error("数据未改变，无需更改数据！")
				}
				this.$refs.editMibaoForm.validate(async valid => {
					if (valid) {
						this.studentselfForm.wenti = this.editMibaoForm.wenti
						this.studentselfForm.mibao = this.editMibaoForm.mibao
						this.$axios.put('student/updateself/' + window.sessionStorage.getItem("username"), this.studentselfForm)
							.then(res => {
								if (res.status !== 200) {
									return this.$message.error("更新密保信息失败！")
								}
								this.studentselfForm = res.data
								this.studentwenti = this.studentselfForm.wenti
								this.studentmibao = this.studentselfForm.mibao
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
			editPwdDialogClosed () {
				this.$refs['editpasswordForm'].resetFields();
			},
			// 保存链接的激活地址
			saveNavState(activePath) {
				this.resetSetItem('activePath',activePath)
				// window.sessionStorage.setItem('activePath',activePath)					
			}
		}
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
	.el-input,.el-textarea {
		width: 50%;
		display: flex;
	}
	.el-radio {
		float: left;
		margin-top: 13px;
	}	
</style>
