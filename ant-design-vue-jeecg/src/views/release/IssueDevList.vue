<template>
  <div>
    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button @click="handleCodeFreeze" type="primary" icon="lock">Code Freeze</a-button>
      <a-button @click="handlePakcage" type="primary" icon="build">打DEV包</a-button>
      <!-- <a-divider type="vertical" /> -->
      <a-button @click="handleUpdateList" type="primary" icon="retweet">更新Issue信息</a-button>
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
        <template slot="tooltopSlot" slot-scope="text,record">
          <a-tooltip>
            <template slot="text">
              {{text}}
            </template>
            {{text}}
          </a-tooltip>
        </template>
        <span slot="link" slot-scope="text,record">
          <a :href="record.issueLink" target="_blank">{{text}}</a>
        </span>
        <span slot="action" slot-scope="text, record">
          <a-popover placement="left" trigger="click">
            <template slot="content">
              <h5>Branches: </h5>
              <ol>
                <li v-for="b in prInfo.branches">
                  <a :href="b.url" target="_blank">{{b.name}}</a>
                </li>
              </ol>
              <h5>Pull Requests: </h5>
              <ol>
                <li v-for="pr in prInfo.pullRequests">
                  [{{pr.status}}] <a :href="pr.url" target="_blank">{{pr.name}}</a>
                </li>
              </ol>
            </template>
            <template slot="title">
              <span>{{ record.issueNum }}</span>
            </template>
            <a @click="handlePr(record)">Pull Requests</a>
          </a-popover>
          <a-divider type="vertical" />
        </span>

      </a-table>
    </div>

    <issue-modal ref="modalForm" @ok="modalFormOk"></issue-modal>
  </div>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import IssueModal from './modules/IssueModal'
  import { getAction, postAction } from '@api/manage'

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
          // {
          //   title:'issue id',
          //   align:"center",
          //   dataIndex: 'issueId'
          // },
          {
            title:'issue号',
            align:"center",
            dataIndex: 'issueNum',
            scopedSlots: { customRender: 'link' }
          },
          {
            title:'issue类型',
            align:"center",
            dataIndex: 'issueType'
          },
          {
            title:'issue名',
            align:"left",
            dataIndex: 'issueName',
            width: 120,
            ellipsis: true,
            customRender: (text, record, index) => {
              if(text.length > 30) {
                return text.slice(0,30) + '...'
              }else{
                return text
              }
            }
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
          historyList: "/release/issueHistory/list",
          issueStatus: "/release/issue/devStatus"
        },
        dictOptions:{},
        superFieldList:[],
        publishlistId: '',
        prInfo: ''
      }
    },
    beforeCreate() {
      this.dataSource = []
    },
    created() {
      this.getSuperFieldList();
      this.publishlistId = this.$route.query.pid || this.pid
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
        const formData = new FormData();
        formData.append('publishlistId', this.publishlistId)
        postAction(this.url.updateIssuesUrl, formData).then((res) => {
          if(res.success) {
            this.$message.success('更新 Jira Issue 成功，可以查在变更历史中查看历史信息。')
            this.loadData()
          }else{
            this.$message.error(res.message)
          }
        })
      },
      handleCodeFreeze(){
        this.$notification.success({message:"check all issues has [Merged] PR"})
      },
      handlePakcage(){
        this.$notification.success({message:"Run Build Dev Package"})

      },
      handlePr(r){
        getAction(this.url.issueStatus, {issueId: r.issueId}).then(res => {
          if(res.success){
            this.prInfo = res.result
            console.log()
          }else{
            this.$message.error(res.message)
          }
        })
      },
      getSuperFieldList(){
        let fieldList=[];
        fieldList.push({type:'string',value:'issueNum',text:'issue号',dictCode:''})
        fieldList.push({type:'string',value:'issueName',text:'issue名',dictCode:''})
        fieldList.push({type:'string',value:'issueType',text:'issue类型',dictCode:''})
        fieldList.push({type:'string',value:'jiraVersionName',text:'jira fix version',dictCode:''})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
  .ant-card-body{padding:0}
</style>