<template>
  <a-table :columns="folderCols" :data-source="folderData" @expand="getJobData">
    <a-table slot="expandedRowRender" slot-scope="folder" :data-source="folder.jobs" :columns="jobCols"
             @expand="getRunData">
      <a-table slot="expandedRowRender" slot-scope="job" :data-source="job.runs" :columns="runCols">
        <template slot="log" slot-scope="run">
          <a @click="getLog(run)">查看</a>
          <a-modal v-model="logVisible" title="Basic Modal">
            <p>{{ logContent }}</p>
          </a-modal>
        </template>
      </a-table>
    </a-table>
  </a-table>
</template>

<script>
import {getAction} from "@api/manage";

export default {
  data() {
    return {
      logVisible: false,
      logContent: '',
      folderData: [],
      folderCols: [
        {title: 'Folder', dataIndex: 'name', key: 'name'},
        {title: 'Disabled', dataIndex: 'disabled', key: 'disabled'}
      ],
      jobCols: [
        {title: 'Job', dataIndex: 'name', key: 'name'},
        {title: 'Disabled', dataIndex: 'disabled', key: 'disabled'}
      ],
      runCols: [
        {title: 'Number', dataIndex: 'number', key: 'number'},
        {title: 'Result', dataIndex: 'result', key: 'result'},
        {title: 'Duration', dataIndex: 'duration', key: 'duration'},
        {title: 'Log', key: 'log', scopedSlots: {customRender: 'log'}},
      ],
    };
  },
  mounted() {
    this.listFolders();
  },
  methods: {
    listFolders() {
      getAction("/blueocean/folders/").then(resp => {
        if (resp.success) {
          const folderData = [];
          for (let i in resp.result) {
            folderData.push({
              key: i,
              name: resp.result[i].name,
              disabled: resp.result[i].disabled ? 'Yes' : 'No',
              jobs: []
            });
          }
          this.folderData = folderData;
        }
      });
    },
    getJobData(expanded, folder) {
      if (expanded) {
        getAction(`/blueocean/folders/${folder.name}/jobs`).then(resp => {
          if (resp.success) {
            const jobData = [];
            for (let i in resp.result) {
              jobData.push({
                key: i,
                name: resp.result[i].name,
                disabled: resp.result[i].disabled ? 'Yes' : 'No',
                folder: folder.name,
                runs: []
              });
            }
            folder.jobs = jobData
          }
        });
      }
    },
    getRunData(expanded, job) {
      if (expanded) {
        getAction(`/blueocean/folders/${job.folder}/jobs/${job.name}/runs`).then(resp => {
          if (resp.success) {
            const jobData = [];
            for (let i in resp.result) {
              jobData.push({
                key: i,
                folder: job.folder,
                job: job.name,
                number: resp.result[i].id,
                result: resp.result[i].result,
                duration: resp.result[i].durationInMillis
              });
            }
            job.runs = jobData
          }
        });
      }
    },
    getLog(run) {

      getAction(`/blueocean/folders/${run.folder}/jobs/${run.job}/runs/${run.number}`).then(resp => {
        if (resp.success) {
          this.logContent = resp.result
          this.logVisible = !this.logVisible
        }
      });
    }
  }
};
</script>
