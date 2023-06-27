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
      <a-button type="primary" icon="download" @click="handleExportXls('固定资产表')">导出</a-button>
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

    <it-fa-modal ref="modalForm" @ok="modalFormOk"></it-fa-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import ItFaModal from './modules/ItFaModal'
  import {filterMultiDictText} from '@/components/dict/JDictSelectUtil'

  export default {
    name: 'ItFaList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      ItFaModal
    },
    data () {
      return {
        description: '固定资产表管理页面',
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
            title:'固定资产编号',
            align:"center",
            dataIndex: 'fixedAssetsNumber'
          },
          {
            title:'设备名称',
            align:"center",
            dataIndex: 'equipmentName'
          },
          {
            title:'设备型号',
            align:"center",
            dataIndex: 'equipmentModel'
          },
          {
            title:'使用状态',
            align:"center",
            dataIndex: 'useState_dictText'
          },
          {
            title:'使用部门',
            align:"center",
            dataIndex: 'useOrgCode'
          },
          {
            title:'保管人员',
            align:"center",
            dataIndex: 'useOwner'
          },
          {
            title:'存放地点',
            align:"center",
            dataIndex: 'site_dictText'
          },
          {
            title:'设备原值',
            align:"center",
            dataIndex: 'equOriValue'
          },
          {
            title:'设备净值',
            align:"center",
            dataIndex: 'equNetValue'
          },
          {
            title:'设备分类',
            align:"center",
            dataIndex: 'equipmentClass_dictText'
          },
          {
            title:'时间',
            align:"center",
            dataIndex: 'itTime',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'备注',
            align:"center",
            dataIndex: 'remarks'
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
          list: "/it/itFa/list",
          delete: "/it/itFa/delete",
          deleteBatch: "/it/itFa/deleteBatch",
          exportXlsUrl: "/it/itFa/exportXls",
          importExcelUrl: "it/itFa/importExcel",
          
        },
        dictOptions:{},
        superFieldList:[],
      }
    },
    created() {
    this.getSuperFieldList();
    },
    computed: {
      importExcelUrl: function(){
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
      },
    },
    methods: {
      initDictConfig(){
      },
      getSuperFieldList(){
        let fieldList=[];
        fieldList.push({type:'string',value:'fixedAssetsNumber',text:'固定资产编号',dictCode:''})
        fieldList.push({type:'string',value:'equipmentName',text:'设备名称',dictCode:''})
        fieldList.push({type:'string',value:'equipmentModel',text:'设备型号',dictCode:''})
        fieldList.push({type:'string',value:'useState',text:'使用状态',dictCode:'use_type'})
        fieldList.push({type:'string',value:'useOrgCode',text:'使用部门',dictCode:''})
        fieldList.push({type:'string',value:'useOwner',text:'保管人员',dictCode:''})
        fieldList.push({type:'string',value:'site',text:'存放地点',dictCode:'it_site'})
        fieldList.push({type:'double',value:'equOriValue',text:'设备原值',dictCode:''})
        fieldList.push({type:'double',value:'equNetValue',text:'设备净值',dictCode:''})
        fieldList.push({type:'string',value:'equipmentClass',text:'设备分类',dictCode:'equ_type'})
        fieldList.push({type:'date',value:'itTime',text:'时间'})
        fieldList.push({type:'string',value:'remarks',text:'备注',dictCode:''})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>