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
<!--      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>-->
<!--      <a-button type="primary" icon="download" @click="handleExportXls('发布单不同状态的package url')">导出</a-button>-->
<!--      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">-->
<!--        <a-button type="primary" icon="import">导入</a-button>-->
<!--      </a-upload>-->
      <a-button-group>
        <a-button @click="handlePakcage" type="primary" icon="build">打{{stage}}包</a-button>
<!--        <a-button @click="handleQuard" type="primary" icon="build">Run Quard</a-button>-->
<!--        <a-button @click="handleStep" type="primary" icon="build">Run Step</a-button>-->
      </a-button-group>
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

        <span slot="linkSlot" slot-scope="text,record">
          <a :href="record.packageUrl" target="_blank">{{text}}</a>
        </span>
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
          <a @click="handleDetail(record)">详情</a>
          <a-divider type="vertical" />
          <a @click="handleQuard(record)" type="primary" icon="build">Quard</a>
          <a-divider type="vertical" />
          <a @click="handleStep(record)" type="primary" icon="build">Step</a>

          <a-divider type="vertical" />
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>
            <a-menu slot="overlay">
              <!-- <a-menu-item>
                <a @click="handleEdit(record)">编辑</a>
              </a-menu-item> -->
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

    <pub-status-package-url-modal ref="modalForm" @ok="modalFormOk"></pub-status-package-url-modal>
<!--    <step-modal ref="stepForm" @ok="stepFormOk"></step-modal>-->
    <test-quard-job-modal ref="quardForm" @ok="quardFormOk"></test-quard-job-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import PubStatusPackageUrlModal from './modules/PubStatusPackageUrlModal'
  import {getAction,postAction} from "@api/manage";
  import TestQuardJobModal from '@views/test/modules/TestQuardJobModal'

  export default {
    name: 'PubStatusPackageUrlList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      PubStatusPackageUrlModal,
      TestQuardJobModal
    },
    props: {
      stage: {
        type: String,
        default: '',
        required: true
      },
      publishForm: {
        type: Object,
        default: {},
        required: true
      }
    },
    data () {
      return {
        description: '发布单不同状态的package url管理页面',
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
          //   title:'产品包url',
          //   align:"center",
          //   dataIndex: 'packageUrl',
          //   scopedSlots: { customRender: 'linkSlot' }
          // },
          {
            title:'包名',
            align:"center",
            dataIndex: 'name',
            scopedSlots: { customRender: 'linkSlot' }
          },
          {
            title:'发布单状态',
            align:"center",
            dataIndex: 'pubStatus'
          },
          {
            title:'存储类型',
            align:"center",
            dataIndex: 'storageType'
          },
          {
            title:'产品线名',
            align:"center",
            dataIndex: 'productLineName'
          },
          {
            title:'产品名',
            align:"center",
            dataIndex: 'productName'
          },
          {
            title:'版本',
            align:"center",
            dataIndex: 'version'
          },
          {
            title:'版本类型',
            align:"center",
            dataIndex: 'versionType'
          },
          {
            title:'发布单id',
            align:"center",
            dataIndex: 'publishlistId'
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
          list: "/release/pubStatusPackageUrl/list",
          delete: "/release/pubStatusPackageUrl/delete",
          deleteBatch: "/release/pubStatusPackageUrl/deleteBatch",
          exportXlsUrl: "/release/pubStatusPackageUrl/exportXls",
          importExcelUrl: "/release/pubStatusPackageUrl/importExcel",
          jenkinsExeJobUrl: "/jenkins/exeJob",
          jenkinsQuardUrl: "/testing/quardJenkins",
          jenkinsStepUrl: "/testing/stepJenkins"
        },
        dictOptions:{},
        superFieldList:[],
      }
    },
    created() {
      this.getSuperFieldList();
      this.loadData()
      console.log("publishForm =======>",  this.publishForm)
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
        console.log('loadData:>>>>', this.stage, arg)
        if(!this.url.list){
          this.$message.error("请设置url.list属性!")
          return
        }
        //加载数据 若传入参数1则加载第一页的内容
        if (arg === 1) {
          this.ipagination.current = 1;
        }
        var params = this.getQueryParams();//查询条件
        params.pubStatus = this.stage
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
      getSuperFieldList(){
        let fieldList=[];
        fieldList.push({type:'string',value:'packageUrl',text:'产品包url',dictCode:''})
        fieldList.push({type:'string',value:'name',text:'包名',dictCode:''})
        fieldList.push({type:'string',value:'pubStatus',text:'发布单状态',dictCode:''})
        fieldList.push({type:'string',value:'storageType',text:'存储类型',dictCode:''})
        fieldList.push({type:'string',value:'productLineName',text:'产品线名',dictCode:'dict_bu'})
        fieldList.push({type:'string',value:'productName',text:'产品名',dictCode:'product'})
        fieldList.push({type:'string',value:'version',text:'版本',dictCode:''})
        fieldList.push({type:'string',value:'versionType',text:'版本类型',dictCode:'version_type'})
        fieldList.push({type:'string',value:'publishlistId',text:'发布单id',dictCode:''})
        this.superFieldList = fieldList
      },
      handlePakcage() {
        let params = {};
        params.folderName = "KE4"
        params.jobName = "0.KE-All-CD-2.2"
        params.paramMap = {
          "deployWay": "Auto Select",
          "deployType": "Regular",
          "envStage": this.stage,
          "customerPkg": "NORMAL",
          "version": this.publishForm.versionName,
        }
        params.typeName = "OFS"
        console.log("params=====> ", params)
        postAction(this.url.jenkinsExeJobUrl, params).then((res) => {
          if (res.success) {
            this.$message.success(res.message);
            this.loadData();
          } else {
            this.$message.warning(res.message);
          }
        })
      },
      handleQuard(record) {
        console.log("run", this.stage, "Quard: ", record)
        this.$refs.quardForm.add(record);
        this.$refs.quardForm.title = "执行 Quard 自动化测试";
        this.$refs.quardForm.disableSubmit = false;
        // let params = {
        //   "packageUrl": p.packageUrl,
        //   "platform": "4X_CDH5.16_ALL",
        //   "quardBranch": "quard-4.x",
        //   "quardRepo": "Kyligence",
        //   "quardType": "KE4X_Daily",
        //   "reuseNum": p.version,
        //   "selectTests": "",
        //   "upgradeNum": "4.6.11.0"
        // }
        // console.log("params=====> ", params)
        // postAction(this.url.jenkinsQuardUrl, params).then((res) => {
        //   if (res.success) {
        //     this.$message.success(res.message);
        //     this.loadData();
        //   } else {
        //     this.$message.warning(res.message);
        //   }
        // })
      },
      handleStep(p) {
        console.log("run", this.stage, "Step: ", p)
        let params = {
          "checkTime": "600",
          "envStage": this.stage,
          "fileName": p.packageUrl,
          "isCh": true,
          "planName": "",
          "productline": "",
          "redeployGw05": true,
          "redeployGw06": true,
          "redeployGw08": true,
          "versionName": ""
        }
        console.log("params=====> ", params)
        postAction(this.url.jenkinsStepUrl, params).then((res) => {
          if (res.success) {
            this.$message.success(res.message);
            this.loadData();
          } else {
            this.$message.warning(res.message);
          }
        })
      },
      quardFormOk(){},
      stepFormOk(){},
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>