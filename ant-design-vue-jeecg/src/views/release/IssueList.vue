<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
<!--    <div class="table-page-search-wrapper">-->
<!--      <a-form layout="inline" @keyup.enter.native="searchQuery">-->
<!--        <a-row :gutter="24">-->
<!--        </a-row>-->
<!--      </a-form>-->
<!--    </div>-->
    <!-- 查询区域-END -->

    <!-- 操作按钮区域 -->
    <div class="table-operator">
<!--      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>-->
      <a-button @click="handleUpdateList" type="primary" icon="retweet">更新Issue信息</a-button>
<!--      <a-button @click="handleHistory" type="primary" icon="inbox">历史变更记录</a-button>-->
      <a-button type="primary" icon="download" @click="handleExportXls('issue本地记录')">导出</a-button>
      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
        <a-button type="primary" icon="import">导入</a-button>
      </a-upload>
      <!-- 高级查询区域 -->
      <j-super-query :fieldList="superFieldList" ref="superQueryModal" @handleSuperQuery="handleSuperQuery"></j-super-query>
      <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay">
          <a-menu-item key="1" @click="batchDel"><a-icon type="delete"/>删除</a-menu-item>
        </a-menu>
        <a-button style="margin-left: 8px"> 批量操作 <a-icon type="down" /></a-button>
      </a-dropdown>
    </div>

    <!-- table区域-begin -->
    <div>
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a style="font-weight: 600">{{ selectedRowKeys.length }}</a>项
        <a style="margin-left: 24px" @click="onClearSelected">清空</a>
      </div>

      <a-table
        ref="table"
        size="middle"
        :scroll="{x:true}"
        bordered
        rowKey="id"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
        class="j-table-force-nowrap"
        @change="handleTableChange">

        <template slot="htmlSlot" slot-scope="text">
          <div v-html="text"></div>
        </template>
        <template slot="imgSlot" slot-scope="text,record">
          <span v-if="!text" style="font-size: 12px;font-style: italic;">无图片</span>
          <img v-else :src="getImgView(text)" :preview="record.id" height="25px" alt="" style="max-width:80px;font-size: 12px;font-style: italic;"/>
        </template>
        <template slot="fileSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px;font-style: italic;">无文件</span>
          <a-button
            v-else
            :ghost="true"
            type="primary"
            icon="download"
            size="small"
            @click="downloadFile(text)">
            下载
          </a-button>
        </template>

        <span slot="action" slot-scope="text, record">
          <a @click="handleEdit(record)">编辑</a>

          <a-divider type="vertical" />
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a @click="handleDetail(record)">详情</a>
              </a-menu-item>
              <a-menu-item>
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>

      </a-table>
    </div>

    <issue-modal ref="modalForm" @ok="modalFormOk"></issue-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import IssueModal from './modules/IssueModal'
  import { getAction, putAction, postAction } from '@api/manage'

  export default {
    name: 'IssueList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      IssueModal
    },
    props: {
      pid:{
        type: String,
        default: '',
        required: true
      },
    },
    data () {
      return {
        description: 'issue本地记录管理页面',
        // 表头
        columns: [
          {
            title: '#',
            dataIndex: '',
            key:'rowIndex',
            width:60,
            align:"center",
            customRender:function (t,r,index) {
              return parseInt(index)+1;
            }
          },
          {
            title:'issue号',
            align:"center",
            dataIndex: 'issueNum'
          },
          {
            title:'issue名',
            align:"left",
            dataIndex: 'issueName',
            width: 220,
            customRender: (text, record, index) => {
              if(text.length > 50) {
                return text.slice(0,50) + '...'
              }else{
                return text
              }
            }
          },
          {
            title:'issue类型',
            align:"center",
            dataIndex: 'issueType'
          },
          // {
          //   title:'jira的issue链接',
          //   align:"center",
          //   dataIndex: 'issueLink'
          // },
          {
            title:'发布单号',
            align:"center",
            dataIndex: 'publishlistId'
          },
          {
            title:'项目id',
            align:"center",
            dataIndex: 'projectId'
          },
          {
            title:'jira版本名',
            align:"center",
            dataIndex: 'jiraVersionName'
          },
          {
            title: '操作',
            dataIndex: 'action',
            align:"center",
            fixed:"right",
            width:147,
            scopedSlots: { customRender: 'action' }
          }
        ],
        url: {
          list: "/release/issue/list",
          delete: "/release/issue/delete",
          deleteBatch: "/release/issue/deleteBatch",
          exportXlsUrl: "/release/issue/exportXls",
          importExcelUrl: "/release/issue/importExcel",
          updateIssuesUrl: "/release/issue/updateIssueList",
          historyList: "/release/issueHistory/list"
        },
        dictOptions:{},
        superFieldList:[],
        publishlistId: ''
      }
    },
    beforeCreate() {
      console.log('beforeCreate->',this.dataSource)
      this.dataSource = []
    },
    created() {
      this.getSuperFieldList();
      this.publishlistId = this.$route.query.pid || this.pid
      console.log('issueList created->', this.$route.query.pid, this.pid, this.queryParam)
    },
    computed: {
      importExcelUrl: function(){
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
      },
    },
    methods: {
      initDictConfig(){
      },
      loadData(arg) {
        if(!this.url.list){
          this.$message.error("请设置url.list属性!")
          return
        }
        //加载数据 若传入参数1则加载第一页的内容
        if (arg === 1) {
          this.ipagination.current = 1;
        }
        var params = this.getQueryParams();//查询条件
        params.publishlistId = this.pid
        console.log("params", params)
        this.loading = true;
        getAction(this.url.list, params).then((res) => {
          if (res.success) {
            //update-begin---author:zhangyafei    Date:20201118  for：适配不分页的数据列表------------
            this.dataSource = res.result.records||res.result;
            if(res.result.total) {
              this.ipagination.total = res.result.total;
            }else{
              this.ipagination.total = 0;
            }
            //update-end---author:zhangyafei    Date:20201118  for：适配不分页的数据列表------------
          }else{
            this.$message.warning(res.message)
          }
        }).finally(() => {
          this.loading = false
        })
      },
      handleUpdateList() {
        let params = {}
        params.publishlistId = this.publishlistId
        postAction(this.url.updateIssuesUrl, params).then((res) => {
          if(res.success) {
            console.log(res)
            this.loadData()
          }else{
            this.$message.error(res.message)
          }
        })
      },
      getSuperFieldList(){
        let fieldList=[];
        fieldList.push({type:'string',value:'issueNum',text:'jira的issue号',dictCode:''})
        fieldList.push({type:'string',value:'issueName',text:'jira的issue名',dictCode:''})
        fieldList.push({type:'string',value:'issueType',text:'jira的issue类型',dictCode:''})
        fieldList.push({type:'string',value:'issueLink',text:'jira的issue链接',dictCode:''})
        fieldList.push({type:'string',value:'publishlistId',text:'发布单号',dictCode:''})
        fieldList.push({type:'string',value:'projectId',text:'项目id',dictCode:''})
        fieldList.push({type:'string',value:'jiraVersionName',text:'jira版本名',dictCode:''})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>