<template>
  <div class="auth-control">
    <el-container class="auth-box">
      <el-header  class="sub-header">
        <el-row style="height: 100%">
          <el-col :span="4">
            <span style="float:left;
            line-height: 40px;
            margin:0;"><b>权限控制</b></span>
          </el-col>
        </el-row>
      </el-header>
      <el-row style="margin-top: 10px; margin-bottom: 0">
        <el-col :span="4" style="margin-left: 5%;">
          <el-button @click="addRole($event)" type="primary" style="float: left;">
            添加角色
          </el-button>
        </el-col>
      </el-row>
      <el-main class="main" v-loading="this.mainLoad">
        <el-table
          class="main-table"
          :data="tableData"
          style="margin-bottom: 20px"
          row-key="id"
          stripe
          border
          max-height="800px"
          default-expand-all
        >
          <el-table-column prop="id" align="center" label="编号"  width="70" />
          <el-table-column prop="name" align="center" label="名称"  />
          <el-table-column prop="roleKey" align="center" label="key"  />
          <el-table-column align="center" label="操作" width="300px" >
            <template #default="scope">
              <el-button size="small" type="success"
                         @click="controlAuth(scope.$index, $event)">角色管理</el-button>
              <el-button size="small" type="danger"
                         @click="removeRole(scope.$index, $event)">移除角色</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-main>
    </el-container>
    <el-dialog v-model="openAddDialog" title="添加角色">
      <el-form :model="roleForm" :rules="roleRules" ref= "roleForm">
        <el-form-item prop="name" label="名称：" :label-width="80" >
          <el-input v-model="roleForm.name" autocomplete="off"
                    style="width: 50%;
                           float: left"
          />
        </el-form-item>
        <el-form-item  prop="key" label="键：" :label-width="80" >
          <el-input v-model="roleForm.key"
                    autocomplete="off"
                    style="width: 50%; float: left"
          />
        </el-form-item>
        <el-button type="primary" @click="submitForm('roleForm', $event)">确定</el-button>
      </el-form>

    </el-dialog>
    <!-- -->
    <el-dialog v-model="openAuthDialog" title="权限管控" class="ACDialog" width="70%" >
      <el-form :model="roleForm" :rules="roleRules" ref= "roleForm">
        <el-row>
          <el-col style="height: 40px" :offset="1">
            <span style="float: left">编号：{{this.role.id}}</span>
          </el-col>
        </el-row>
        <el-form-item prop="name" label="名称：" :label-width="80" >
          <el-input v-model="roleForm.name" autocomplete="off"
                    style="width: 50%;
                           float: left"
          />
        </el-form-item>
        <el-form-item  prop="key" label="键：" :label-width="80" >
          <el-input v-model="roleForm.key"
                    autocomplete="off"
                    style="width: 50%; float: left"
          />
        </el-form-item>
        <el-button type="primary" @click="submitForm('roleForm', $event)">确定</el-button>
        <el-row style="margin-bottom: 0">
          <el-col>
            <h2 style="float: left; margin-bottom: 0">权限分配</h2>
          </el-col>
          <el-col>
            <h4 style="float: left; margin-bottom: 0; color: #42b983">* 修改权限后请重新登录</h4>
          </el-col>
        </el-row>
        <el-row>
          <el-col>
            <hr/>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="11">
            <el-table
              class="rest-menu-table"
              :data="roleRestMenuTable"
              style="margin-bottom: 20px"
              row-key="id"
              max-height="400px"
              default-expand-all
            >
              <el-table-column prop="menuName" align="center" label="名称" />
              <el-table-column prop="perm" align="center" label="key" />
              <el-table-column align="center" label="操作" width="50px" >
                <template #default="scope">
                  <el-button style="width: 18px; height: 18px; padding: 0;
                                    min-height: 18px; border: 0"
                             type="success"
                             @click="addMenu(scope.$index, $event)"
                  >
                    <el-icon><Plus/></el-icon>
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-col>
          <el-col :span="11" :offset="2">
            <el-table
              class="menu-table"
              :data="roleMenuTable"
              style="margin-bottom: 20px"
              row-key="id"
              max-height="400px"
              default-expand-all
            >
              <el-table-column prop="menuName" align="center" label="名称" />
              <el-table-column prop="perm" align="center" label="key" />
              <el-table-column align="center" label="操作" width="50px" >
                <template #default="scope">
                  <el-button size="small"
                             style="width: 18px; height: 18px; padding: 0;
                                    min-height: 18px; border: 0"
                             type="danger"
                             @click="removeMenu(scope.$index, $event)"
                  >
                    <el-icon><Minus/></el-icon>
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-col>
        </el-row>
      </el-form>

    </el-dialog>
  </div>
</template>

<script>
import { getAllRoleAPI, saveOrUpdateRoleAPI,
  removeRoleAPI, getRoleMenuAPI, getRestRoleMenuAPI,
  addRomeMenuAPI, removeRomeMenuAPI } from '@/api/role'
import { CirclePlus, Plus, Minus } from '@element-plus/icons'

export default {
  name: 'AuthView',
  components: {
    CirclePlus,
    Plus,
    Minus

  },
  data () {
    const validateName = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('名称不可为空'))
      }
      callback()
    }
    const validateKey = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('键不可为空'))
      }
      callback()
    }
    return {
      openAuthDialog: false,
      openAddDialog: false,
      authLoad: false,
      role: null,
      mainLoad: false,
      tableData: [],
      roleForm: {
        name: "",
        key: "",
      },
      roleRules: {
        name: [
          { validator:validateName, require: true, trigger: 'blur' }
        ],
        key: [
          {  validator:validateKey, require: true, trigger: 'blur' }
        ]
      },
      roleMenuTable: [],
      roleRestMenuTable: []
    }
  },
  methods: {
    addRole(event) {
      this.myBlur(event)
      this.role = null
      this.roleForm.name = ""
      this.roleForm.key = ""
      this.openAddDialog = true
    },
    removeRole(index, event) {
      removeRoleAPI(this.tableData[index].id).then(
        (res) => {
          console.log(res)
          if(res.data.success) {
            this.tableData.splice(index, 1)
          }else {
            this.$message.error(res.data.message)
          }
        }
      ).catch(
        (err) => {
          this.$message.error(err.message)
        }
      )
    },
    submitForm (formName, event) {
      this.myBlur(event)
      this.$refs[formName].validate((valid) => {
        if (valid) {
          const pack = {
            id: (this.role === null || this.role.id === 0) ? null : this.role.id,
            name: this.roleForm.name,
            roleKey: this.roleForm.key,
            delFlag: 0
          }
          console.log(pack)
          saveOrUpdateRoleAPI(pack)
            .then(
              (res) => {
                if(res.data.success) {
                  this.$message.success("修改成功")
                  this.requestRoleTable()
                  this.openAddDialog = false
                }else {
                  this.$message.error(data.message)
                }
              }
            ).catch(
            (err) => {
              this.$message.error(err.message)
            }
          )
        }else {
          console.log('error submit!!')
          return false
        }
      })
    },
    myBlur(event) {
      let tar = event.target
      while (tar.nodeName !== "BUTTON") {
        tar = tar.parentNode
      }
      tar.blur()
    },
    requestRoleTable() {
      this.mainLoad = true
      getAllRoleAPI().then(
        (res) => {
          if(res.data.success) {
            this.tableData = res.data.data
            this.mainLoad = false
          }else {
            this.$message.error(data.message)
          }
        }).catch(
        (err) => {
          this.$message.error(err.message)
        }
      )
    }
    ,
    controlAuth(index, event) {
      this.myBlur(event)
      this.openAuthDialog = true
      //this.authLoad = true
      this.role = this.tableData[index]
      this.roleForm.name = this.role.name
      this.roleForm.key = this.role.roleKey
      getRoleMenuAPI(this.role.id).then(
        (res) => {
          console.log(res.data.data)
          if(res.data.success) {
            this.roleMenuTable = res.data.data
          }else {
            this.$message.error(res.data.message)
          }
        }
      ).catch(
        (err) => {
          this.$message.error(err.message)
        }
      )
      getRestRoleMenuAPI(this.role.id).then(
        (res) => {
          console.log(res.data)
          if(res.data.success) {
            this.roleRestMenuTable = res.data.data
          }else {
            this.$message.error(res.data.message)
          }
        }
      ).catch(
        (err) => {
          this.$message.error(err.message)
        }
      )
    },
    addMenu(index, event) {
      this.myBlur(event)
      addRomeMenuAPI(this.role.id, this.roleRestMenuTable[index].id)
        .then(
          (res) => {
            if (res.data.success) {
              let menu = this.roleRestMenuTable[index]
              this.roleMenuTable.push(menu)
              this.roleRestMenuTable.splice(index, 1)
            }else {
              this.$message(res.data.message)
            }
          }
        ).catch(
        (err) => {
          this.$message.error(err.message)
        }
      )
    },
    removeMenu(index, event) {
      this.myBlur(event)
      removeRomeMenuAPI(this.role.id, this.roleMenuTable[index].id)
        .then(
          (res) => {
            if (res.data.success) {
              let menu = this.roleMenuTable[index]
              this.roleRestMenuTable.push(menu)
              this.roleMenuTable.splice(index, 1)
            }else {
              this.$message(res.data.message)
            }
          }
        ).catch(
        (err) => {
          this.$message.error(err.message)
        }
      )
    },
  },
  mounted () {
    this.requestRoleTable()
  }
}
</script>

<style scoped>
.auth-control {
  background-color: #f3f4f7;
  width: 86%;
  margin-left: 14%;
  height: auto;
  float: right;
  margin-top: 40px;
  margin-right: 0;
  padding-right: 0;
}
.sub-header {
  background-color: white;
  height: 40px;
}

.main {
  margin-top: 1%;
}
.main-table {
  width: 90%;
  margin-left: 5%;
  height: 640px;
}

/deep/ .ACDialog {
  width: 80%;
}
</style>
