<template>
  <div class="department-view">
    <el-container class="department-box">
      <el-header  class="sub-header">
        <el-row style="height: 100%">
          <el-col :span="4">
            <span style="float:left;
            line-height: 40px;
            margin:0;"><b>部门管理</b></span>
          </el-col>
        </el-row>
      </el-header>
      <el-row style="margin-top: 10px; margin-bottom: 10px">
        <el-col :span="4" style="margin-left: 5%;">
          <el-button @click="addDepartment($event)" type="primary" style="float: left;">
            添加新部门
          </el-button>
        </el-col>
      </el-row>
      <el-main class="main" ref="mainWindow" v-loading="this.mainLoading">
        <el-card style="width: 90%; margin-left: 5%">
          <template #header>
            <div class="card-header" style="height: 16px;">
              <span style="float: left">部门</span>
            </div>
          </template>
          <el-collapse :model="this.departmentList" ref="board">
            <div v-for="(value, key) in departmentList" :key="value.id">
              <el-collapse-item class="department-item">
                <template #title>
                  <el-col :span="18" style="text-align: left">{{value.name}}</el-col>
                  <el-col :span="4" style="height: 30px;" :offset="2">
                    <el-button style="margin-left: 10px" class="title-button" type="danger"
                               @click="deleteDepartment(key, value.id, $event)" plain>
                      <el-icon><Delete /></el-icon>
                    </el-button>
                    <el-button class="title-button" type="primary" @click="editDepartment(value.id, $event)" plain>
                      <el-icon><Edit /></el-icon>
                    </el-button>
                    <el-button class="title-button" type="success" @click="addPosition(value.id, $event)" plain>
                      <el-icon><Plus /></el-icon>
                    </el-button>
                  </el-col>
                </template>
                <el-row v-for="(item, index) in positionList[value.id]" :key="item.id" class="position-item" >
                    <el-col :span="18" style="float: left; height: 20px; vertical-align: center">
                    <span style="height: 100%;
                    font-size: 14px;
                    float: left;
                    margin-left: 40px;
                     text-align: left">{{item.name}}</span>
                    </el-col>
                    <el-col :span="4" style="float: right; height: 20px;" :offset="2">
                      <el-button type="primary"
                                 circle @click="editPosition(value.id, item.id, $event)">
                        <el-icon ><Edit /></el-icon>
                      </el-button>
                      <el-button style="margin-left: 0" type="danger" circle
                                 @click="deletePosition(value.id, item.id, $event)">
                        <el-icon><Delete /></el-icon>
                      </el-button>
                    </el-col>
                </el-row>
              </el-collapse-item>
            </div>
          </el-collapse>
        </el-card>
      </el-main>
    </el-container>

    <el-dialog v-loading="posLoad" v-model="openPositionDialog" title="编辑岗位信息">
      <el-form :model="posForm" :rules="posRules" ref= "posForm">
        <el-form-item prop="name" label="岗位名称" :label-width="80" >
          <el-input v-model="posForm.name" autocomplete="off" />
        </el-form-item>
        <el-form-item  prop="desc" label="简介" :label-width="80" >
          <el-input v-model="posForm.desc"
                    autocomplete="off"
                    maxlength="500"
                    type="textarea"
                    show-word-limit
                    autosize/>
        </el-form-item>
      </el-form>
      <template #footer>
      <span class="dialog-footer">
        <el-button type="primary" @click="submitForm('posForm')">确定</el-button>
        <el-button @click="closeForm('posForm')">返回</el-button>

      </span>
      </template>
    </el-dialog>


    <el-dialog v-loading="depLoad" v-model="openDepartmentDialog" title="编辑部门信息">
      <el-form :model="depForm" :rules="depRules" ref="depForm">
        <el-form-item prop="name" label="部门名称" :label-width="80" >
          <el-input v-model="depForm.name" autocomplete="off" />
        </el-form-item>
        <el-form-item  prop="desc" label="简介" :label-width="80" >
          <el-input v-model="depForm.desc"
                    autocomplete="off"
                    maxlength="500"
                    type="textarea"
                    show-word-limit
                    autosize/>
        </el-form-item>
      </el-form>
      <template #footer>
      <span class="dialog-footer">
        <el-button type="primary" @click="submitForm('depForm')">确定</el-button>
        <el-button @click="closeForm('depForm')">返回</el-button>

      </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { Delete, Edit, Plus } from '@element-plus/icons'
import { deleteDepartmentAPI, getAllBasicAPI,
  getDepartmentInfoAPI, saveOrUpdateDepartmentAPI } from '@/api/department'
import {
  getAllBasicByDepartmentIdAPI, deletePositionAPI,
  updatePositionAPI, getPositionInfoAPI, saveOrUpdatePositionAPI
} from '@/api/position'
import { loginAPI } from '@/api/login'

const formLabelWidth = '140px'

export default {
  name: 'DepartmentView',
  components: {
    Plus,
    Delete,
    Edit,
  },
  data () {
    const validateName = (rule, value, callback) => {
        if (value === '') {
          callback(new Error('名称不可为空'))
        }
        callback()
      }
    return {
      mainLoading: true,
      departmentList: [],
      positionList: {},
      openPositionDialog: false,
      openDepartmentDialog: false,
      posLoad: false,
      depLoad: false,
      //
      posForm: {
        id: 0,
        father_id: 0,
        name: '',
        desc: ''
      },
      posRules: {
        name: [
          { validator:validateName, require: true, trigger: 'blur' }
        ],
        desc: [
          {  trigger: 'blur' }
        ]
      },
      //
      depForm: {
        id: 0,
        name: '',
        desc: ''
      },
      depRules: {
        name: [
          { validator:validateName, require: true, trigger: 'blur' }
        ],
        desc: [
          { trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    closeForm(formName) {
      if (formName === "depForm") {
        this.openDepartmentDialog = false
        this.depForm.id = 0
        this.depForm.desc = ""
        this.depForm.name = ""
      }else {
        this.openPositionDialog = false
        this.posForm.desc = ""
        this.posForm.id = 0
        this.posForm.name = ""
        this.posForm.father_id = 0
      }
    },
    myBlur(event) {
      let tar = event.target
      while (tar.nodeName !== "BUTTON") {
        tar = tar.parentNode
      }
      tar.blur()
    },
    //
    addDepartment (event) {
      this.myBlur(event)
      this.openDepartmentDialog = true
      this.depForm.id = 0
      this.depForm.desc = ""
      this.depForm.name = ""
    }
    ,
    deleteDepartment(key, id, event) {
      event.stopPropagation();
      this.myBlur(event)
      deleteDepartmentAPI(id).then(
        (res) => {
          if (res.data.success) {
            this.departmentList.splice(key, 1)
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
    editDepartment(id, event) {
      event.stopPropagation();
      this.myBlur(event)
      this.depLoad = true
      this.openDepartmentDialog = true
      this.depForm.id = id
      getDepartmentInfoAPI(id).then(
        (res) => {
          if (res.data.success) {
            let data = res.data.data
            this.depForm.name = data.name
            this.depForm.desc = data.desc
            this.depLoad = false
          } else {
            this.$message.error(res.data.message)
          }
        }
      ).catch(
        (err) => {
          this.$message.error(err.message)
        }
      )
    },
    //
    addPosition (father_id, event) {
      event.stopPropagation();
      this.myBlur(event)
      this.openPositionDialog = true
      this.posForm.desc = ""
      this.posForm.id = 0
      this.posForm.name = ""
      this.posForm.father_id = father_id
      //console.log(this.$refs['depForm'])
    },
    deletePosition(father_id, id, event) {
      event.stopPropagation();
      this.myBlur(event)
      console.log(id)
      deletePositionAPI(id).then(
        (res) => {
          if (res.data.success) {
            let pos = 0
            for (pos = 0; pos < this.positionList[father_id].length; ++pos) {
              if (this.positionList[father_id][pos].id === id)
                break
            }
            if (pos < this.positionList[father_id].length) {
              this.positionList[father_id].splice(pos, 1)
            }
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
    editPosition(father_id, id, event) {
      event.stopPropagation();
      this.myBlur(event)
      this.posLoad = true
      this.openPositionDialog = true
      this.posForm.id = id
      // console.log(id)
      getPositionInfoAPI(id).then(
        (res) => {
          if (res.data.success) {
            let data = res.data.data
            this.posForm.name = data.name
            this.posForm.desc = data.desc
            this.posForm.father_id = father_id
            this.posLoad = false
          } else {
            this.$message.error(res.data.message)
          }
        }
      ).catch(
        (err) => {
          this.$message.error(err.message)
        }
      )
    },
    submitForm (formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          if (formName === "posForm") {
            const data = {
              id: this.posForm.id,
              departmentId: this.posForm.father_id,
              name: this.posForm.name,
              description: this.posForm.desc
            }
            saveOrUpdatePositionAPI(data).then(
              (res) => {
                if (res.data.success) {
                  getAllBasicByDepartmentIdAPI(this.posForm.father_id).then (
                    (res) => {
                      if (res.data.success) {
                        this.positionList[this.posForm.father_id] = []
                        let data2 = res.data.data
                        for (let t in data2) {
                          this.positionList[this.posForm.father_id].push(
                            {
                              id: data2[t].id,
                              name: data2[t].name
                            }
                          )
                        }
                      }
                    }
                  )
                  this.$message.success("编辑成功")
                  this.openPositionDialog = false
                }else {
                  this.$message.error(res.data.message)
                }
              }
            ).catch(
              (err) => {
                this.$message.error(err.message)
              }
            )
          }else if (formName === "depForm") {
            const data = {
              id: this.depForm.id,
              name: this.depForm.name,
              description: this.depForm.desc
            }
            this.departmentList = []
            saveOrUpdateDepartmentAPI(data).then(
              (res) => {
                if (res.data.success) {
                  if (this.depForm.id === 0) {
                    getAllBasicAPI().then(
                      (res) => {
                        if (res.data.success) {
                          let data = res.data.data
                          for (let i in data) {
                            // console.log(data[i])
                            this.departmentList.push(
                              {
                                id: data[i].id,
                                name: data[i].name
                              }
                            )
                          }
                        }
                      }
                    )
                  }else {
                    for (let i = 0; i < this.departmentList.length; ++i) {
                      if (this.departmentList[i].id === this.depForm.id)
                      {
                        this.$set(
                          this.departmentList, i,
                          {id: this.depForm.id, name: this.depForm.name}
                        )
                        break;
                      }
                    }
                  }
                  this.$message.success("编辑成功")
                  this.openDepartmentDialog = false
                }else {
                  this.$message.error(res.data.message)
                }
              }
            ).catch(
              (err) => {
                this.$message.error(err.message)
              }
            )
          }
        } else {
          console.log('error submit!!')
          return false
        }
      })
    }
  }
  ,
  mounted () {
    getAllBasicAPI().then(
      (res) => {
        if (res.data.success) {
          let data = res.data.data
          for (let i in data) {
            // console.log(data[i])
            this.departmentList.push(
              {
                id: data[i].id,
                name: data[i].name
              }
            )
          }
          // 获取岗位信息
          for (let j = 0; j < this.departmentList.length; ++j) {
            getAllBasicByDepartmentIdAPI(this.departmentList[j].id).then(
              (res) => {
                this.positionList[this.departmentList[j].id] = []
                let data2 = res.data.data
                for (let t in data2) {
                  this.positionList[this.departmentList[j].id].push(
                    {
                      id: data2[t].id,
                      name: data2[t].name
                    }
                  )
                }
              }
            )
          }
          // console.log(this.positionList)
          this.mainLoading = false
        } else if (res.data.code === 401) {
          this.$message.error("认证过期，请重新登陆")
        }
        else {
          this.$message.error(res.data.message)
        }
      }
    ).catch(
      (err) => {
        this.$message.error(err.message)
        return false
      }
    )

  }
}
</script>

<style scoped>
.department-view {
  background-color: #f3f4f7;
  width: 86%;
  margin-left: 14%;
  height: 600px;
  float: right;
  margin-top: 40px;
  margin-right: 0;
  padding-right: 0;
}

.sub-header {
  background-color: white;
  height: 40px;
}
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
/deep/ .el-collapse-item__content {
  padding-bottom: 0px;
}

.position-item {
  margin-bottom: 15px;
  height: 20px;
  border-top: #f3f4f7 solid 1px;
  font-size: 10px;
  text-align: center;
   margin-right: 5px;
}
.department-item {
  border-top: #dadadb solid 1px;

}
.title-button {
  height: 28px;
  float: right;
  width: 28px;
  margin-right: 10px;
  padding: 0;
  font-size: 10px;
  min-height: 5px;
}
.position-item .el-button{
  height: 28px;
  float: right;
  width: 28px;
  margin-right: 5px;
  margin-left: 15px;
  padding: 0;
  font-size: 10px;
  min-height: 5px;
}
.position-item .el-icon {
  font-size: 15px;
}
</style>
