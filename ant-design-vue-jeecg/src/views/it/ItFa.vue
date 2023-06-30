<template>
  <a-card :bordered="false">
    <a-row>
      <!-- 操作按钮区域 -->
      <div class="table-operator">
        <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
      </div>
    </a-row>
    <a-row :gutter="8">
      <!-- 左侧父 -->
      <a-col :span="12">
        <j-vxe-table
          row-number
          row-selection
          row-selection-type="radio"
          click-select-row
          highlight-current-row
          :radio-config="{highlight: false}"
          :checkbox-config="{highlight: false}"
          :height="950"
          :loading="table1.loading"
          :columns="table1.columns"
          :dataSource="table1.dataSource"
          :pagination="table1.pagination"
          @pageChange="handleTable1PageChange"
          @selectRowChange="handleTable1SelectRowChange"
        />
      </a-col>
      <a-col :span="12">

        <j-vxe-table
          row-number
          row-selection
          click-select-row
          :height="456"
          :loading="table3.loading"
          :columns="table3.columns"
          :dataSource="table3.dataSource"
          :pagination="table3.pagination"
          @pageChange="handleTable3PageChange"
          @selectRowChange="handleTable3SelectRowChange"
        />
        <!-- 左侧选择的数据展示在这里 -->
        <j-vxe-table
          row-number
          row-selection
          click-select-row
          highlight-current-row
          :height="456"
          :loading="table2.loading"
          :columns="table2.columns"
          :dataSource="table2.dataSource"
          :pagination="table2.pagination"
          @pageChange="handleTable2PageChange"
          @selectRowChange="handleTable2SelectRowChange"
        />

        <!-- 右下子 -->
      </a-col>
    </a-row>

    <it-fa-modal ref="modalForm" @ok="modalFormOk"></it-fa-modal>
  </a-card>
</template>

<script>
import { getAction } from '@api/manage'
import { JVXETypes } from '@/components/jeecg/JVxeTable'
import ItFaModal from './modules/ItFaModal'

// 【多种布局模板】 左边选择后，记录选到右侧，右侧是父、子
export default {
  name: 'ItFa',
  components: {
    ItFaModal
  },
  data() {
    return {
      table1: {
        // 是否正在加载
        loading: false,
        // 分页器参数
        pagination: {
          // 当前页码
          current: 1,
          // 每页的条数
          pageSize: 20,
          // 可切换的条数
          pageSizeOptions: ['10', '20', '30', '100', '200'],
          // 数据总数（目前并不知道真实的总数，所以先填写0，在后台查出来后再赋值）
          total: 0,
        },
        // 最后选中的行
        lastRow: null,
        // 选择的行
        selectedRows: [],
        // 数据源，控制表格的数据
        dataSource: [],
        // 列配置，控制表格显示的列
        columns: [
          {
            title:'固定资产编号',
            align:"center",
            key: 'fixedAssetsNumber',
            width: '130px',
          },
          {
            title:'设备分类',
            align:"center",
            key: 'equipmentClass_dictText',
            width: '120px'
          },
          {
            title:'设备名称',
            align:"center",
            key: 'equipmentName',
            width: '120px'
          },
          {
            title:'设备型号',
            align:"center",
            key: 'equipmentModel',
            width: '120px'
          },
          {
            title:'使用状态',
            align:"center",
            key: 'useState_dictText',
            width: '60px'
          },
          {
            title:'使用部门',
            align:"center",
            key: 'useOrgCode',
            width: '120px'
          },
          {
            title:'保管人员',
            align:"center",
            key: 'useOwner',
            width: '80px'
          },
          {
            title:'存放地点',
            align:"center",
            key: 'site_dictText',
            width: '120px'
          },
          // {
          //   title:'设备原值',
          //   align:"center",
          //   key: 'equOriValue'
          // },
          // {
          //   title:'设备净值',
          //   align:"center",
          //   key: 'equNetValue'
          // },
          // {
          //   title:'时间',
          //   align:"center",
          //   key: 'itTime',
          //   customRender:function (text) {
          //     return !text?"":(text.length>10?text.substr(0,10):text)
          //   }
          // },
        ],
      },
      // 子级表的配置信息 （配置和主表的完全一致，就不写冗余的注释了）
      table2: {
        loading: false,
        pagination: {current: 1, pageSize: 200, pageSizeOptions: ['100', '200'], total: 0},
        selectedRows: [],
        dataSource: [],
        columns: [
          {
            title:'固定资产编号',
            align:"center",
            key: 'fixAssertsId',
            width: '180px'
          },
          {
            title:'净值',
            align:"center",
            key: 'netValue',
            width: '120px'
          },
          {
            title:'年',
            align:"center",
            key: 'year',
            width: '120px'
          },
          {
            title:'月',
            align:"center",
            key: 'month',
            width: '120px'
          },
          {
            title:'日',
            align:"center",
            key: 'day',
            width: '120px'
          },
        ],
      },
      table3: {
        loading: false,
        pagination: {current: 1, pageSize: 200, pageSizeOptions: ['100', '200'], total: 0},
        selectedRows: [],
        dataSource: [],
        columns: [
          {
            title:'固定资产id',
            align:"center",
            key: 'fixAssertsId',
            width: '120px',
          },
          {
            title:'原保管人员',
            align:"center",
            key: 'fromOwnName',
            width: '120px',
          },
          {
            title:'原部门',
            align:"center",
            key: 'fromDepartment',
            width: '120px',
          },
          {
            title:'原存放地点',
            align:"center",
            key: 'fromLocation',
            width: '120px',
          },
          {
            title:'原使用状态',
            align:"center",
            key: 'fromUseStatus',
            width: '120px',
          },
          {
            title:'目的保管人员',
            align:"center",
            key: 'toOwnerName',
            width: '120px',
          },
          {
            title:'目的部门',
            align:"center",
            key: 'toDepartment',
            width: '120px',
          },
          {
            title:'目的存放地点',
            align:"center",
            key: 'toLocation',
            width: '120px',
          },
          {
            title:'目的使用状态',
            align:"center",
            key: 'toUseStatus',
            width: '120px',
          },
        ],
      },
      // 查询url地址
      url: {
        getData: '/mock/vxe/getData',
        getItFaUrl: "/it/itFa/list",
        itNetList: "/it/itFixAssetsNetValue/list",
        itTransferRecordList: "/it/itFixAssertsTransferRecord/list",
      },
    }
  },
  // 监听器
  watch: {
    // 监听table1 【主表】选择的数据发生了变化
    ['table1.lastRow']() {
      this.loadTable2Data()
      this.loadTable3Data()
    },

  },
  created() {
    this.loadTable1Data()
  },
  methods: {

    // 加载table1【主表】的数据
    loadTable1Data() {
      // 封装查询条件
      let formData = {
        pageNo: this.table1.pagination.current,
        pageSize: this.table1.pagination.pageSize
      }
      // 调用查询数据接口
      this.table1.loading = true
      getAction(this.url.getItFaUrl, formData).then(res => {
        if (res.success) {
          // 后台查询回来的 total，数据总数量
          this.table1.pagination.total = res.result.total
          // 将查询的数据赋值给 dataSource
          this.table1.dataSource = res.result.records
          // 重置选择
          this.table1.selectedRows = []
        } else {
          this.$error({title: '主表查询失败', content: res.message})
        }
      }).finally(() => {
        // 这里是无论成功或失败都会执行的方法，在这里关闭loading
        this.table1.loading = false
      })
    },
    
    // 加载table2【子表】的数据，根据主表的id进行查询
    loadTable2Data() {
      // 如果主表没有选择，则不查询
      let selectedRows = this.table1.selectedRows
      let formData = {
        pageNo: this.table2.pagination.current,
        pageSize: this.table2.pagination.pageSize,
        fixAssertsId: this.table1.lastRow.fixedAssetsNumber
      }
      this.table2.loading = true
      getAction(this.url.itNetList, formData).then(res => {
        if (res.success) {
          this.table2.pagination.total = res.result.total
          this.table2.dataSource = res.result.records
          this.table2.selectedRows = []
        } else {
          this.$error({title: '子表查询失败', content: res.message})
        }
      }).finally(() => {
        this.table2.loading = false
      })
    },

    // 加载table3【子表】的数据，根据主表的id进行查询
    loadTable3Data() {
      // 如果主表没有选择，则不查询
      let selectedRows = this.table1.selectedRows
      let formData = {
        pageNo: this.table3.pagination.current,
        pageSize: this.table3.pagination.pageSize,
        fixAssertsId: this.table1.lastRow.fixedAssetsNumber
      }
      this.table3.loading = true
      getAction(this.url.itTransferRecordList, formData).then(res => {
        if (res.success) {
          this.table3.pagination.total = res.result.total
          this.table3.dataSource = res.result.records
          this.table3.selectedRows = []
        } else {
          this.$error({title: '子表查询失败', content: res.message})
        }
      }).finally(() => {
        this.table3.loading = false
      })
    },

    // table1【主表】当选择的行变化时触发的事件
    handleTable1SelectRowChange(event) {
      this.handleTableSelectRowChange(this.table1, event)
    },

    // table2【子表】当选择的行变化时触发的事件
    handleTable2SelectRowChange(event) {
      this.table2.selectedRows = event.selectedRows
    },

    // table3【子表】当选择的行变化时触发的事件
    handleTable3SelectRowChange(event) {
      this.table3.selectedRows = event.selectedRows
    },

    // 当table1【主表】分页参数变化时触发的事件
    handleTable1PageChange(event) {
      // 重新赋值
      this.table1.pagination.current = event.current
      this.table1.pagination.pageSize = event.pageSize
      // 查询数据
      this.loadTable1Data()
    },

    // 当table2【子表】分页参数变化时触发的事件
    handleTable2PageChange(event) {
      // 重新赋值
      this.table2.pagination.current = event.current
      this.table2.pagination.pageSize = event.pageSize
      // 查询数据
      this.loadTable2Data()
    },

    /** 公共方法：处理表格选中变化事件 */
    handleTableSelectRowChange(table, event) {
      let {row, action, selectedRows, $table} = event
      // 获取最后一个选中的
      let lastSelected = selectedRows[selectedRows.length - 1]
      if (action === 'selected') {
        table.lastRow = row
      } else if (action === 'selected-all') {
        // 取消全选
        if (selectedRows.length === 0) {
          table.lastRow = null
        } else if (!table.lastRow) {
          table.lastRow = lastSelected
        }
      } else if (action === 'unselected' && row === table.lastRow) {
        table.lastRow = lastSelected
      }
      $table.setCurrentRow(table.lastRow)
      table.selectedRows = selectedRows
    },

  }
}
</script>

<style scoped>

</style>