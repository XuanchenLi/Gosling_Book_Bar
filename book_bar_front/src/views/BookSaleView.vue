<template>
  <div class="sale-view">
    <el-container class="salary-box" >
      <el-header  class="sub-header">
        <el-row style="height: 100%">
          <el-col :span="4">
            <span style="float:left;
            line-height: 40px;
            margin:0;"><b>书籍销售统计</b></span>
          </el-col>
        </el-row>
      </el-header>
      <el-main class="main" v-loading="mainLoad">
        <el-menu
          :default-active="'0'"
          class="menu"
          mode="horizontal"
          :ellipsis="false"
          @select="handleSelect"
        >
          <el-menu-item index="0">销售进货量</el-menu-item>
          <el-menu-item index="1">总书籍销售额</el-menu-item>
        </el-menu>
        <el-card class="box-card" v-show="view[0]">
          <template #header>
            <div class="card-header">
              <span>书籍销售量</span>
              <el-select
              v-model="this.books"
              multiple
              placeholder="Select"
              style="width: 240px"
              @change="handleChange"
              filterable
              remote
              :remote-method="getSuggest"
              :loading="loading"
              >
                <el-option
                  v-for="item in books"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </div>
          </template>
          <div id="bookStats" style="width: 800px;height:400px;"></div>
        </el-card>
        <el-card class="box-card" v-show="view[1]">
          <template #header>
            <div class="card-header">
              <span>书籍销售额</span>
            </div>
          </template>
          <div id="bookLine" style="width: 800px;height:400px;"></div>

          <div id="bookPie" style="width: 800px;height:400px;"></div>
        </el-card>
      </el-main>
    </el-container>
  </div>
</template>

<script>
import { getBookStatsSuggestAPI, getBookStatsSaleAPI, getBookStatsInventoryAPI
          , getBookTypeSaleAPI, getBookAllSaleAPI} from '@/api/bookStats'
import * as echarts from 'echarts'

export default {
  name: 'BookSaleView',
  data() {
    return {
      lastBooks: [],
      books: [],
      mainLoad: false,
      loading: false,
      lastView: 0,
      view: [true, false],
      bookStatsChart: null,
      bookStatsOptions: {
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: {
          type: 'time',
          axisLabel: {
            formatter: (value) => {
              if (value) {
                var oDate = new Date(value),
                  oYear = oDate.getFullYear(),
                  oMonth = oDate.getMonth() + 1,
                  oTime = oYear + '\n' + this.addZero(oMonth)
                return oTime;
              } else {
                return ""
              }
            },
            // ...
          }
        },
        legend: {
          orient: 'vertical',
          x: 'right',
          data: []
        },
        tooltip: {
          trigger: 'item',
          show: true,
          axisPointer: {            // 坐标轴指示器，坐标轴触发有效
            type: 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
          },
          formatter: (params) => {
            let data = params.data
            //console.log(params)
            let str = params.seriesName.substr(0, params.seriesName.length - 1) + "<br/>"
              + data[2] + "本" + "<br/>" + Math.abs(data[1]) + "元"
            return str
          }
        },
        yAxis: {
          axisLabel: {
            formatter: '{value}元'
          }
        },
        series: [],
      },
      bookLineChart: null,
      boolPieChart: null,
      bookLineOptions: {
        title: {
          text: "月度书籍销售额"
        },
        xAxis: {
          type: 'time',
          axisLabel: {
            formatter: (value) => {
              if (value) {
                var oDate = new Date(value),
                  oYear = oDate.getFullYear(),
                  oMonth = oDate.getMonth() + 1,
                  oTime = oYear + '\n' + this.addZero(oMonth)
                return oTime;
              } else {
                return ""
              }
            },
          },
        },
        tooltip: {
          trigger: 'item',

        },
        yAxis: {
          type: 'value',
          axisLabel: {
            formatter: '{value}元'
          }
        },
        series: [
          {
            data: [],
            type: 'line'
          }
        ]
      },
      bookPieOptions: {
        title: {
          text: "",
          left:'left'
        },
        tooltip : {
          show: true,
          trigger: 'item',
          axisPointer: {            // 坐标轴指示器，坐标轴触发有效
            type: 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
          },
          formatter: "{b}-{c}元"

        },
        legend: {
          orient: 'vertical',
          x: 'right',
          data: [],
        },
        series: [
          {
            type: 'pie',
            data: [],
            avoidLabelOverlap: false,
            label: {
              normal: {
                show: true,
                //position: "inner",
                textStyle: {
                  fontWeight: 200,
                  fontSize: 18, //文字的字体大小
                },
                formatter: function (params) {
                  //console.log(params)
                  if (params.data.value === 0) {
                    //console.log(params.data)
                    params.data.labelLine.show = false
                    return ""
                  } else {
                    return params.name + '-' + params.percent + '%'
                  }
                }
              },
            }
          }
        ]
      },

    }
  },
  methods: {
    addZero(num) {
      if (parseInt(num) < 10) {
        num = "0" + num
      }
      return num
    },
    getUnion(a, b) {
      return a.concat(b.filter(function(v) {
        return a.indexOf(v) === -1})).sort()
    },
    getMinus(a, b) {
      let newArr= a.filter(function(v){
        return b.indexOf(v) === -1
      })
      return newArr
    },
    handleSelect(index) {
      this.view[this.lastView] = false
      this.view[index] = true
      this.lastView = index
    },
    handleChange(val) {
      //console.log(val)
      //console.log(this.books)
      if (this.books.length > this.lastBooks.length) {
        let ext = this.getMinus(this.books, this.lastBooks)[0]
        //console.log(ext)
        getBookStatsSaleAPI(ext).then(
          (res) => {
            if (res.data.success) {
              let data = res.data.data
              if (data.length === 0)
                return
              //console.log("Sale", data)
              var graph = {
                mid: data[0]['id'],
                //stats: data,
                data: [],
                type: 'bar',
                stack: data[0]['bookName'] + "（" + data[0]['bookLabel'] + "）",
                label: {
                  show: true,
                  position: 'inside'
                },
                name: data[0]['bookName'] + "（" + data[0]['bookLabel'] + "）" + "销售量",
                lineStyle: {
                  normal: {
                    width: 3,
                  }
                },
                barWidth : 20,
              }
              this.bookStatsOptions.legend.data.push(data[0]['bookName'] + "（"
                + data[0]['bookLabel'] + "）" + "销售量")
              for (let i in data) {
                if (data[i]['saleTime'] === "" || data[i]['saleTime'] === null)
                  continue
                graph.data.push(
                  [data[i]['saleTime'], -parseInt(data[i]['sale']), parseInt(data[i]['saleNum'])]
                )
              }
              this.bookStatsOptions.series.push(graph)
              //console.log(this.bookStatsOptions)
              this.bookStatsChart.setOption(this.bookStatsOptions)
            } else {
              this.$message.error(res.data.message)
            }
          }
        ).catch(
          (err) => {
            this.$message.error(err.message);
          }
        )
        getBookStatsInventoryAPI(ext).then(
          (res) => {
            if (res.data.success) {
              let data = res.data.data
              //console.log("In", data)
              var graph = {
                mid: data[0]['id'],
                data: [],
                //stats: data,
                type: 'bar',
                stack: data[0]['bookName'] + "（" + data[0]['bookLabel'] + "）",
                name: data[0]['bookName'] + "（" + data[0]['bookLabel'] + "）" + "购入量",
                label: {
                  show: true,
                  position: 'inside'
                },
                lineStyle: {
                  normal: {
                    width: 3,
                    type: 'dashed'
                  }
                },
                barWidth : 20,
              }
              this.bookStatsOptions.legend.data.push(data[0]['bookName'] + "（"
                + data[0]['bookLabel'] + "）" + "购入量")
              for (let i in data) {
                graph.data.push(
                  [data[i]['inventoryTime'], parseInt(data[i]['inventory']), parseInt(data[i]['inNum'])]
                )
              }
              this.bookStatsOptions.series.push(graph)
              //console.log(this.bookStatsOptions)
              this.bookStatsChart.setOption(this.bookStatsOptions)
            } else {
              this.$message.error(res.data.message)
            }
          }
        ).catch(
          (err) => {
            this.$message.error(err.message);
          }
        )
      }else {
        let ext = this.getMinus(this.lastBooks, this.books)[0]
        //console.log(ext, this.bookStatsOptions.series[0])
        for(let i in this.bookStatsOptions.series) {
          if (this.bookStatsOptions.series[i].mid === ext){
            this.bookStatsOptions.series.splice(i, 1);
          }
        }
        for(let i in this.bookStatsOptions.series) {
          if (this.bookStatsOptions.series[i].mid === ext){
            this.bookStatsOptions.series.splice(i, 1);
          }
        }
        this.bookStatsChart.setOption(this.bookStatsOptions, true)
        //console.log(this.bookStatsOptions)
      }
      this.lastBooks = this.books
    },
    handleRemove(val) {
      //console.log(val)
    },
    getSuggest(query) {
      if (query) {
        this.loading = true
        getBookStatsSuggestAPI(query)
          .then(
            (res) => {
              if (res.data.success) {
                let data = res.data.data
                var timeout
                clearTimeout(timeout)
                timeout = setTimeout(
                  () => {
                    this.options = []
                    //console.log(data)
                    for (let i in data) {
                      this.options.push(
                        {
                          value: data[i]['id'],
                          label: data[i]['bookName'] + "（" + data[i]['bookLabel'] + "）",
                        }
                      )
                    }
                    this.loading = false
                  }, 300)
              }else {
                this.$message.error(res.data.message)
              }
            }
          ).catch(
          (err) => {
            this.$message.error(err.message);
          }
        )
      } else {
      }
    },
  },
  mounted () {
    this.bookStatsChart = echarts.init(document.getElementById('bookStats'))
    this.bookStatsChart.setOption(this.bookStatsOptions)
    this.mainLoad = true

    getBookAllSaleAPI()
      .then(
        (res) => {
          if (res.data.success) {
            let data = res.data.data
            for (let i in data) {
              this.bookLineOptions.series[0].data.push(
                [data[i]['str'], parseFloat(data[i]['num'])]
              )
            }
            this.bookLineChart = echarts.init(document.getElementById('bookLine'))
            this.bookLineChart.setOption(this.bookLineOptions)

          }else {
            this.$message.error(res.data.message)
          }
        }
      ).catch(
      (err) => {
        this.$message.error(err.message)
      }
    )

    getBookTypeSaleAPI()
      .then(
        (res) => {
          if (res.data.success) {
            let data = res.data.data
            let total = 0
            for (let i in data) {
              total += parseFloat(data[i]['num'])
              this.bookPieOptions.legend.data.push(data[i]['str'])
              this.bookPieOptions.series[0]['data'].push(
                {
                  value: parseFloat(data[i]['num']),
                  name: data[i]['str'],
                  labelLine: {
                    show: true
                  }
                }
              )
            }
            this.bookPieOptions.title.text = "总销售额：" + total + "元"
            this.bookPieChart = echarts.init(document.getElementById('bookPie'))
            this.bookPieChart.setOption(this.bookPieOptions)
            this.mainLoad = false
            console.log(data)
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
}
</script>

<style scoped>
.sale-view {
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
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.menu .el-menu-item {
  background-color: #ffffff;

}
.box-card {
  width: 80%;
  margin-left: 10%;
  margin-top: 20px;
}
</style>
