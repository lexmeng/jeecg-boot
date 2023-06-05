<template>
  <a-timeline>
    <a-timeline-item v-for="item in timeLines" :key="item.issueNum" :color="item.color">
      批次：{{ item.batchNum }} , 添加 Issue {{ item.addCounts }} 个，移除 Issue {{ item.subCounts }} 个。
      <a-collapse :bordered="false" style="padding-top: 10px;" accordion v-if="item.hasItem">
        <a-collapse-panel key="1" header="添加的 Issue" v-if="item.addIssueList.length>0">
          <a-list :data-source="item.addIssueList">
            <a-list-item slot="renderItem" slot-scope="addItem, index">
              <a-list-item-meta :description="addItem.issueName">
                <a target="_blank" slot="title" :href="addItem.issueLink">{{ addItem.issueNum }}</a>
              </a-list-item-meta>
              <div>{{ addItem.issueType }}</div>
            </a-list-item>
          </a-list>
        </a-collapse-panel>
        <a-collapse-panel key="2" header="移除的 Issue" v-if="item.subIssueList.length>0">
          <a-list :data-source="item.subIssueList">
            <a-list-item slot="renderItem" slot-scope="subItem, index">
              <a-list-item-meta :description="subItem.issueName">
                <a target="_blank" slot="title" :href="subItem.issueLink">{{ subItem.issueNum }}</a>
              </a-list-item-meta>
            </a-list-item>
          </a-list>
        </a-collapse-panel>
      </a-collapse>
    </a-timeline-item>
  </a-timeline>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import IssueHistoryModal from './modules/IssueHistoryModal'
  import {getAction} from "@api/manage";

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
          listBatch: "/release/issueHistory/listBatch",
        },
        dictOptions:{},
        superFieldList:[],
        publishlistId: '',
        timeLines: []
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
    watch: {
      dataSource: function(val) {
        console.log('dataSource======> ', val)
        let datas = _.map(this.dataSource, function (item) {
          console.log('item======> ', item);
          let timeItem = item
          timeItem.addCounts = item.addIssueList ? item.addIssueList.length : 0
          timeItem.subCounts = item.subIssueList ? item.subIssueList.length : 0
          timeItem.hasItem = timeItem.addCounts + timeItem.subCounts > 0
          if(timeItem.addCounts > 0) {
            timeItem.color = 'green'
          } else if(timeItem.subCounts > 0) {
            timeItem.color = 'red'
          } else {
            timeItem.color = 'blue'
          }
          return timeItem
        })
        console.log('datas', datas);
        this.timeLines = datas
      }
    },
    computed: {
      importExcelUrl: function(){
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
      }
    },
    methods: {
      initDictConfig(){
      },
      loadData(arg) {
        if(!this.url.listBatch){
          this.$message.error("请设置url.list属性!")
          return
        }
        //加载数据 若传入参数1则加载第一页的内容
        if (arg === 1) {
          this.ipagination.current = 1;
        }
        var queryParam = this.getQueryParams();
        var params = {} //this.getQueryParams();//查询条件
        params.publishlistId = this.pid
        params.pageNo = queryParam.pageNo
        params.pageSize = 100
        this.loading = true;
        getAction(this.url.listBatch, params).then((res) => {
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