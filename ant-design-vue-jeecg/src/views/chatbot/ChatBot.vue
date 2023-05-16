<template>
  <div>
    <a-list
        v-if="comments.length"
        :data-source="comments"
        :header="`${comments.length} ${comments.length > 1 ? 'replies' : 'reply'}`"
        item-layout="horizontal"
    >
      <a-list-item slot="renderItem" slot-scope="item, index">
        <a-comment
            :author="item.author"
            :avatar="item.avatar"
            :content="item.content"
            :datetime="item.datetime"
        />
      </a-list-item>
    </a-list>
    <a-comment>
      <div slot="content">
        <a-form-item>
          <a-textarea :rows="4" :value="value" allow-clear @change="handleChange"/>
        </a-form-item>
        <a-form-item>
          <a-button html-type="submit" :loading="submitting" type="primary" @click="handleSubmit">
            Add Comment
          </a-button>
        </a-form-item>
      </div>
    </a-comment>
  </div>
</template>
<script>

import moment from 'moment';
import {postAction} from "@api/manage";

export default {
  name: "ChatBot",
  data() {
    return {
      comments: [],
      submitting: false,
      value: '',
      moment,
    };
  },
  methods: {
    handleSubmit() {
      if (!this.value) {
        return;
      }

      this.submitting = true;
      this.comments = [
        ...this.comments,
        {
          author: 'user',
          avatar: <a-icon type="user"/>,
          content: this.value,
          datetime: moment().fromNow(),
        },

      ]

      let params = {}
      params.content = this.value

      postAction("/chatbot/call", params).then((resp) => {
        if (resp.success) {
          this.comments = [
            ...this.comments,
            {
              author: resp.result.role,
              avatar: <a-icon type="aliwangwang" theme="filled"/>,
              content: resp.result.content,
              datetime: moment().fromNow(),
            },
          ];
        }
      }).finally(() => {
        this.submitting = false;
        this.value = null;
      })
    },
    handleChange(e) {
      this.value = e.target.value;
    },
  },
};
</script>
