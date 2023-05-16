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
      <a-button type="primary" icon="download" @click="handleExportXls('issue历史表')">导出</a-button>
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

    <issue-history-modal ref="modalForm" @ok="modalFormOk"></issue-history-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import IssueHistoryModal from './modules/IssueHistoryModal'

  export default {
    name: 'IssueHistoryList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      IssueHistoryModal
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
        description: 'issue历史表管理页面',
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
            dataIndex: 'issueNum',
            customRender:function (t,r,index) {
              return '<a href="+r.issueLink+">"+t+"</a>'
            }
          },
          {
            title:'issue名',
            align:"center",
            dataIndex: 'issueName'
          },
          {
            title:'issue类型',
            align:"center",
            dataIndex: 'issueType'
          },
          // {
          //   title:'issue链接',
          //   align:"center",
          //   dataIndex: 'issueLink'
          // },
          // {
          //   title:'发布单号',
          //   align:"center",
          //   dataIndex: 'publishlistId'
          // },
          // {
          //   title:'项目名',
          //   align:"center",
          //   dataIndex: 'projectId'
          // },
          // {
          //   title:'jira版本名',
          //   align:"center",
          //   dataIndex: 'jiraVersionName'
          // },
          {
            title:'当初创建的日期时间',
            align:"center",
            dataIndex: 'issueCreateDatetime'
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
          list: "/release/issueHistory/list",
          delete: "/release/issueHistory/delete",
          deleteBatch: "/release/issueHistory/deleteBatch",
          exportXlsUrl: "/release/issueHistory/exportXls",
          importExcelUrl: "release/issueHistory/importExcel",

        },
        dictOptions:{},
        superFieldList:[],
        publishlistId: ''
      }
    },
    created() {
      this.getSuperFieldList();
      this.publishlistId = this.$route.query.pid
      this.reLoadData()
    },
    computed: {
      importExcelUrl: function(){
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
      },
    },
    methods: {
      initDictConfig(){
      },
      reloadData(pid){
        this.queryParam = {publishlistId: pid || this.publishlistId}
        this.loadData()
      },
      getSuperFieldList(){
        let fieldList=[];
        fieldList.push({type:'string',value:'issueNum',text:'issue号',dictCode:''})
        fieldList.push({type:'string',value:'issueName',text:'issue名',dictCode:''})
        fieldList.push({type:'string',value:'issueType',text:'issue类型',dictCode:''})
        fieldList.push({type:'string',value:'issueLink',text:'issue链接',dictCode:''})
        fieldList.push({type:'string',value:'publishlistId',text:'发布单号',dictCode:''})
        fieldList.push({type:'string',value:'projectId',text:'项目名',dictCode:''})
        fieldList.push({type:'string',value:'jiraVersionName',text:'jira版本名',dictCode:''})
        fieldList.push({type:'datetime',value:'issueCreateDatetime',text:'当初创建的日期时间'})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>