<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->

    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
      <a-button type="primary" icon="download" @click="handleExportXls('发布单')">导出</a-button>
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
          <a v-if="!isPublished(record)" @click="handleEdit(record)">编辑</a>
          <a v-else disabled @click="handleEdit(record)">编辑</a>
          <a-divider type="vertical" />
          <a @click="handleDetail(record)">详情</a>
          <a-divider type="vertical" />
          <a @click="handleDeploy(record)">发布</a>
          <a-divider type="vertical" />
          <a @click="handleIssues(record)">Issues</a>
          <a-divider type="vertical" />
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a @click="handleIssuesHistory(record)">变更历史</a>
              </a-menu-item>
              <a-menu-item>
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                  <a>作废</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>

      </a-table>
    </div>

    <publishlist-modal ref="modalForm" @ok="modalFormOk"/>
    <issue-list-drawer ref="issueList"  @ok="issueListOk" />
    <issue-history-list-drawer ref="issueHistory" @ok="issueListOk" />
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import {initDictOptions, filterDictText} from '@/components/dict/JDictSelectUtil'
  import PublishlistModal from './modules/PublishlistModal'
  import IssueListDrawer from './modules/IssueListDrawer'
  import IssueHistoryListDrawer from './modules/IssueHistoryListDrawer'

  export default {
    name: 'PublishlistList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      IssueHistoryListDrawer,
      PublishlistModal,
      IssueListDrawer
    },
    data () {
      return {
        description: '发布单管理页面',
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
            title:'发布单',
            align:"center",
            dataIndex: 'name'
          },
          {
            title:'产品线',
            align:"center",
            dataIndex: 'productLineName_dictText'
          },
          {
            title:'产品名',
            align:"center",
            dataIndex: 'productName_dictText'
          },
          {
            title:'版本号',
            align:"center",
            dataIndex: 'versionName'
          },
          {
            title:'版本类型',
            align:"center",
            dataIndex: 'versionType_dictText'
          },
          {
            title:'jira版本名',
            align:"center",
            dataIndex: 'jiraVersionName'
          },
          {
            title:'文档版本',
            align:"center",
            dataIndex: 'documentVersion'
          },
          {
            title:'迭代冲刺号',
            align:"center",
            dataIndex: 'scrumNum'
          },
          {
            title:'迭代阶段',
            align:"center",
            dataIndex: 'scrumStage_dictText'
          },
          {
            title:'发布单状态',
            align:"center",
            dataIndex: 'publishlistStage_dictText'
          },
          // {
          //   title:'实际发布时间',
          //   align:"center",
          //   dataIndex: 'publishDatetime'
          // },
          {
            title:'产品经理',
            align:"center",
            dataIndex: 'pmId_dictText'
          },
          // {
          //   title:'产品经理名',
          //   align:"center",
          //   dataIndex: 'pmName'
          // },
          // {
          //   title:'commid id',
          //   align:"center",
          //   dataIndex: 'commitId'
          // },
          // {
          //   title:'用户手册中文链接',
          //   align:"center",
          //   dataIndex: 'urerManualEnLink'
          // },
          // {
          //   title:'用户手册英文链接',
          //   align:"center",
          //   dataIndex: 'userManualChLink'
          // },
          {
            title: '操作',
            dataIndex: 'action',
            align:"center",
            fixed:"right",
            width:280,
            scopedSlots: { customRender: 'action' },
          }
        ],
        url: {
          list: "/release/list",
          delete: "/release/delete",
          deleteBatch: "/release/deleteBatch",
          exportXlsUrl: "/release/exportXls",
          importExcelUrl: "release/importExcel",
        },
        buDictOptions:{},
        productDictOptions:{},
        superFieldList:[],
      }
    },
    created() {
      this.getSuperFieldList();
      this.initDictConfig();
      // console.log('user',this.$store.state.user.info.realname)
    },
    computed: {
      importExcelUrl: function(){
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
      },
    },
    methods: {
      initDictConfig(){
        //初始化字典 - 性别
        initDictOptions('bu').then((res) => {
          if (res.success) {
            this.buDictOptions = res.result;
          }
        });
        initDictOptions('product').then((res) => {
          if (res.success) {
            this.productDictOptions = res.result;
          }
        });
      },
      isPublished: function(record){
        return record.publishlistStage && record.publishlistStage === 'done'
      },
      handleDeploy(record){
        this.$router.push({ path: '/release/publish', query: {id:record.id,record:record} })
      },
      handleIssues(record){
        // console.log("handleIssues: ", record)
        // this.$router.push({ path: '/release/issueList', query: {pid:record.id} })
        this.$refs.issueList.list(record)
        this.$refs.issueList.title = "发布单【" + record.name + "】- Jira Issues"
        this.$refs.issueList.disableSubmit = true
      },
      handleIssuesHistory(record){
        this.$refs.issueHistory.list(record.id)
        this.$refs.issueHistory.title = "发布单【" + record.name + "】- Jira Issues 变更历史"
        this.$refs.issueHistory.disableSubmit = true
      },
      issueListOk(){

      },
      getSuperFieldList(){
        let fieldList=[];
        fieldList.push({type:'string',value:'name',text:'发布单名',dictCode:''})
        fieldList.push({type:'string',value:'productLineName',text:'产品线',dictCode:'dict_bu'})
        fieldList.push({type:'string',value:'productName',text:'产品',dictCode:'product'})
        fieldList.push({type:'string',value:'versionName',text:'版本号',dictCode:''})
        fieldList.push({type:'string',value:'versionType',text:'版本类型',dictCode:'version_type'})
        fieldList.push({type:'string',value:'jiraVersionName',text:'jira版本名',dictCode:''})
        fieldList.push({type:'string',value:'documentVersion',text:'文档版本',dictCode:''})
        fieldList.push({type:'string',value:'scrumNum',text:'迭代冲刺号',dictCode:''})
        fieldList.push({type:'string',value:'scrumStage',text:'迭代阶段',dictCode:'sprint_stage'})
        fieldList.push({type:'string',value:'publishlistStage',text:'发布单状态',dictCode:'release_form_state'})
        fieldList.push({type:'string',value:'publishDatetime',text:'发布时间',dictCode:''})
        fieldList.push({type:'string',value:'pmId',text:'产品经理id',dictCode:'username,realname,sys_user'})
        fieldList.push({type:'string',value:'pmName',text:'产品经理名',dictCode:''})
        fieldList.push({type:'string',value:'commitId',text:'commid id',dictCode:''})
        fieldList.push({type:'string',value:'urerManualEnLink',text:'用户手册中文链接',dictCode:''})
        fieldList.push({type:'string',value:'userManualChLink',text:'用户手册英文链接',dictCode:''})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>