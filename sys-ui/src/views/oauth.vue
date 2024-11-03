<script>

import {countUnRead} from "@/api/system/notice";
import cache from "@/plugins/cache";
import {Notification} from "element-ui";

export default {
  created() {
    const { params, query } = this.$route
    this.$store.dispatch("OauthGitlab", query.code).then(async () => {
      this.$router.push({path: this.redirect || "/"}).catch(() => {});
      await this.$store.dispatch('OpenSocket');
      this.welcome();
    }).catch(() => {});
  },
  render: function(h) {

  },
  welcome() {
    countUnRead().then(res => {
      let timeNow = new Date();
      let hours = timeNow.getHours();
      let text = '';
      if (hours >= 0 && hours <= 10) {
        text = '早上好[';
      } else if (hours > 10 && hours <= 14) {
        text = '中午好[';
      } else if (hours > 14 && hours <= 18) {
        text = '下午好[';
      } else if (hours > 18 && hours <= 24) {
        text = '晚上好[';
      }
      text = text + cache.local.getUserName() + "]";
        if(res.data > 0){
          text = text + ',  您有' + res.data + '条未读消息!!'
        }
      Notification.warning(text);
    });
  }
}
</script>
