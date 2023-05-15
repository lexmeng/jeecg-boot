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
        bordered
        rowKey="id"
        class="j-table-force-nowrap"
        :scroll="{x:true}"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
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
                <a @click="handleIssues(record)">变更历史</a>
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
    <issue-list-drawer ref="issueList" @ok="issueListOk" />
  </a-card>
</template>

<script>

  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import PublishlistModal from './modules/PublishlistModal'
  import IssueListDrawer from '@views/release/modules/IssueListDrawer'
  import {initDictOptions, filterDictText} from '@/components/dict/JDictSelectUtil'
  import '@/assets/less/TableExpand.less'

  export default {
    name: "PublishList",
    mixins:[JeecgListMixin],
    components: {
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
            title:'发布单名',
            align:"center",
            dataIndex: 'name'
          },
          {
            title:'产品线',
            align:"center",
            dataIndex: 'bu_dictText',
            customRender: (text, record, index) => {
              //字典值替换通用方法
              return filterDictText(this.buDictOptions, text);
            }
          },
          {
            title:'产品名',
            align:"center",
            dataIndex: 'product_dictText',
            customRender: (text, record, index) => {
              //字典值替换通用方法
              return filterDictText(this.productDictOptions, text);
            }
          },
          {
            title:'版本号',
            align:"center",
            dataIndex: 'versionName'
          },
          {
            title:'版本类型',
            align:"center",
            dataIndex: 'versionType'
          },
          {
            title:'jira版本名',
            align:"center",
            dataIndex: 'jiraVersionName'
          },
          {
            title:'迭代冲刺号',
            align:"center",
            dataIndex: 'scrumNum'
          },
          {
            title:'迭代阶段',
            align:"center",
            dataIndex: 'scrumStage'
          },
          {
            title:'发布单状态',
            align:"center",
            dataIndex: 'publishlistStage'
          },
          {
            title:'实际发布时间',
            align:"center",
            dataIndex: 'publishDatetime'
          },
          {
            title:'产品经理id',
            align:"center",
            dataIndex: 'pmId'
          },
          {
            title:'产品经理名',
            align:"center",
            dataIndex: 'pmName'
          },
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
    },
    computed: {
      importExcelUrl: function(){
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
      }
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
      handleDetail: function (record) {
        this.$router.push({ path: '/release/publish', query: {record:record} })
      },
      handleDeploy(record){
        console.log("handleDeploy: ", record)
        // this.$router.push({ path: '/release/publish', query: {record:record} })
      },
      handleIssues(record){
        // console.log("handleIssues: ", record)
        // this.$router.push({ path: '/release/issueList', query: {pid:record.id} })
        this.$refs.issueList.list(record)
        this.$refs.issueList.title = "发布单【" + record.name + "】 Issues"
        this.$refs.modalForm.disableSubmit = true
      },
      issueListOk(){

      },
      getSuperFieldList(){
        let fieldList=[];
        fieldList.push({type:'string',value:'name',text:'发布单名',dictCode:''})
        fieldList.push({type:'string',value:'productLineId',text:'产品线id',dictCode:''})
        fieldList.push({type:'string',value:'productLineName',text:'产品线名',dictCode:''})
        fieldList.push({type:'string',value:'productId',text:'产品id',dictCode:''})
        fieldList.push({type:'string',value:'productName',text:'产品名',dictCode:''})
        fieldList.push({type:'string',value:'versionName',text:'版本名',dictCode:''})
        fieldList.push({type:'string',value:'versionType',text:'版本类型',dictCode:''})
        fieldList.push({type:'string',value:'jiraVersionName',text:'jira版本名',dictCode:''})
        fieldList.push({type:'string',value:'documentVersion',text:'文档版本',dictCode:''})
        fieldList.push({type:'string',value:'scrumNum',text:'迭代冲刺号',dictCode:''})
        fieldList.push({type:'string',value:'scrumStage',text:'迭代阶段',dictCode:''})
        fieldList.push({type:'string',value:'publishlistStage',text:'发布单状态',dictCode:''})
        fieldList.push({type:'string',value:'publishDatetime',text:'发布时间',dictCode:''})
        fieldList.push({type:'string',value:'pmId',text:'产品经理id',dictCode:''})
        fieldList.push({type:'string',value:'pmName',text:'产品经理名',dictCode:''})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>