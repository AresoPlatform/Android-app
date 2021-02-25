package com.xw.aschwitkey.entity;

import java.math.BigDecimal;
import java.util.List;

public class PostBean {

    private List<ResultBean> result;

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean {
        private int id;
        private String author;
        private String permlink;
        private String category;
        private String parent_author;
        private String parent_permlink;
        private String title;
        private String body;
        private String jsonData;
        private String last_update;
        private String created;
        private String active;
        private String last_payout;
        private int depth;
        private int children;
        private String net_rshares;
        private String abs_rshares;
        private String vote_rshares;
        private String children_abs_rshares;
        private String cashout_time;
        private String max_cashout_time;
        private int total_vote_weight;
        private int reward_weight;
        private String total_payout_value;
        private String curator_payout_value;
        private int author_rewards;
        private int net_votes;
        private String root_author;
        private String root_permlink;
        private String max_accepted_payout;
        private int percent_steem_dollars;
        private boolean allow_replies;
        private boolean allow_votes;
        private boolean allow_curation_rewards;
        private String url;
        private String root_title;
        private String pending_payout_value;
        private String total_pending_payout_value;
        private String author_reputation;
        private String promoted;
        private int body_length;
        private List<?> beneficiaries;
        private List<ActiveVotesBean> active_votes;
        private List<?> replies;
        private List<?> reblogged_by;
        private Integer commentNum;
        private BigDecimal xasAmount;
        private String headImage;
        private String buildingName;
        private double latitude;
        private double longitude;
        private BigDecimal postValueXas;
        private Integer xasCountNum;
        private boolean isAschWk;
        private boolean isComment;
        private String timeQuantum;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getPermlink() {
            return permlink;
        }

        public void setPermlink(String permlink) {
            this.permlink = permlink;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getParent_author() {
            return parent_author;
        }

        public void setParent_author(String parent_author) {
            this.parent_author = parent_author;
        }

        public String getParent_permlink() {
            return parent_permlink;
        }

        public void setParent_permlink(String parent_permlink) {
            this.parent_permlink = parent_permlink;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getBody() {
            return body;
        }

        public void setBody(String body) {
            this.body = body;
        }

        public String getJsonData() {
            return jsonData;
        }

        public void setJsonData(String jsonData) {
            this.jsonData = jsonData;
        }

        public String getLast_update() {
            return last_update;
        }

        public void setLast_update(String last_update) {
            this.last_update = last_update;
        }

        public String getCreated() {
            return created;
        }

        public void setCreated(String created) {
            this.created = created;
        }

        public String getActive() {
            return active;
        }

        public void setActive(String active) {
            this.active = active;
        }

        public String getLast_payout() {
            return last_payout;
        }

        public void setLast_payout(String last_payout) {
            this.last_payout = last_payout;
        }

        public int getDepth() {
            return depth;
        }

        public void setDepth(int depth) {
            this.depth = depth;
        }

        public int getChildren() {
            return children;
        }

        public void setChildren(int children) {
            this.children = children;
        }

        public String getNet_rshares() {
            return net_rshares;
        }

        public void setNet_rshares(String net_rshares) {
            this.net_rshares = net_rshares;
        }

        public String getAbs_rshares() {
            return abs_rshares;
        }

        public void setAbs_rshares(String abs_rshares) {
            this.abs_rshares = abs_rshares;
        }

        public String getVote_rshares() {
            return vote_rshares;
        }

        public void setVote_rshares(String vote_rshares) {
            this.vote_rshares = vote_rshares;
        }

        public String getChildren_abs_rshares() {
            return children_abs_rshares;
        }

        public void setChildren_abs_rshares(String children_abs_rshares) {
            this.children_abs_rshares = children_abs_rshares;
        }

        public String getCashout_time() {
            return cashout_time;
        }

        public void setCashout_time(String cashout_time) {
            this.cashout_time = cashout_time;
        }

        public String getMax_cashout_time() {
            return max_cashout_time;
        }

        public void setMax_cashout_time(String max_cashout_time) {
            this.max_cashout_time = max_cashout_time;
        }

        public int getTotal_vote_weight() {
            return total_vote_weight;
        }

        public void setTotal_vote_weight(int total_vote_weight) {
            this.total_vote_weight = total_vote_weight;
        }

        public int getReward_weight() {
            return reward_weight;
        }

        public void setReward_weight(int reward_weight) {
            this.reward_weight = reward_weight;
        }

        public String getTotal_payout_value() {
            return total_payout_value;
        }

        public void setTotal_payout_value(String total_payout_value) {
            this.total_payout_value = total_payout_value;
        }

        public String getCurator_payout_value() {
            return curator_payout_value;
        }

        public void setCurator_payout_value(String curator_payout_value) {
            this.curator_payout_value = curator_payout_value;
        }

        public int getAuthor_rewards() {
            return author_rewards;
        }

        public void setAuthor_rewards(int author_rewards) {
            this.author_rewards = author_rewards;
        }

        public int getNet_votes() {
            return net_votes;
        }

        public void setNet_votes(int net_votes) {
            this.net_votes = net_votes;
        }

        public String getRoot_author() {
            return root_author;
        }

        public void setRoot_author(String root_author) {
            this.root_author = root_author;
        }

        public String getRoot_permlink() {
            return root_permlink;
        }

        public void setRoot_permlink(String root_permlink) {
            this.root_permlink = root_permlink;
        }

        public String getMax_accepted_payout() {
            return max_accepted_payout;
        }

        public void setMax_accepted_payout(String max_accepted_payout) {
            this.max_accepted_payout = max_accepted_payout;
        }

        public int getPercent_steem_dollars() {
            return percent_steem_dollars;
        }

        public void setPercent_steem_dollars(int percent_steem_dollars) {
            this.percent_steem_dollars = percent_steem_dollars;
        }

        public boolean isAllow_replies() {
            return allow_replies;
        }

        public void setAllow_replies(boolean allow_replies) {
            this.allow_replies = allow_replies;
        }

        public boolean isAllow_votes() {
            return allow_votes;
        }

        public void setAllow_votes(boolean allow_votes) {
            this.allow_votes = allow_votes;
        }

        public boolean isAllow_curation_rewards() {
            return allow_curation_rewards;
        }

        public void setAllow_curation_rewards(boolean allow_curation_rewards) {
            this.allow_curation_rewards = allow_curation_rewards;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getRoot_title() {
            return root_title;
        }

        public void setRoot_title(String root_title) {
            this.root_title = root_title;
        }

        public String getPending_payout_value() {
            return pending_payout_value;
        }

        public void setPending_payout_value(String pending_payout_value) {
            this.pending_payout_value = pending_payout_value;
        }

        public String getTotal_pending_payout_value() {
            return total_pending_payout_value;
        }

        public void setTotal_pending_payout_value(String total_pending_payout_value) {
            this.total_pending_payout_value = total_pending_payout_value;
        }

        public String getAuthor_reputation() {
            return author_reputation;
        }

        public void setAuthor_reputation(String author_reputation) {
            this.author_reputation = author_reputation;
        }

        public String getPromoted() {
            return promoted;
        }

        public void setPromoted(String promoted) {
            this.promoted = promoted;
        }

        public int getBody_length() {
            return body_length;
        }

        public void setBody_length(int body_length) {
            this.body_length = body_length;
        }

        public List<?> getBeneficiaries() {
            return beneficiaries;
        }

        public void setBeneficiaries(List<?> beneficiaries) {
            this.beneficiaries = beneficiaries;
        }

        public List<ActiveVotesBean> getActive_votes() {
            return active_votes;
        }

        public void setActive_votes(List<ActiveVotesBean> active_votes) {
            this.active_votes = active_votes;
        }

        public List<?> getReplies() {
            return replies;
        }

        public void setReplies(List<?> replies) {
            this.replies = replies;
        }

        public List<?> getReblogged_by() {
            return reblogged_by;
        }

        public void setReblogged_by(List<?> reblogged_by) {
            this.reblogged_by = reblogged_by;
        }

        public Integer getCommentNum() {
            return commentNum;
        }

        public void setCommentNum(Integer commentNum) {
            this.commentNum = commentNum;
        }

        public BigDecimal getXasAmount() {
            return xasAmount;
        }

        public void setXasAmount(BigDecimal xasAmount) {
            this.xasAmount = xasAmount;
        }

        public String getHeadImage() {
            return headImage;
        }

        public void setHeadImage(String headImage) {
            this.headImage = headImage;
        }

        public String getBuildingName() {
            return buildingName;
        }

        public void setBuildingName(String buildingName) {
            this.buildingName = buildingName;
        }

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        public BigDecimal getPostValueXas() {
            return postValueXas;
        }

        public void setPostValueXas(BigDecimal postValueXas) {
            this.postValueXas = postValueXas;
        }

        public Integer getXasCountNum() {
            return xasCountNum;
        }

        public void setXasCountNum(Integer xasCountNum) {
            this.xasCountNum = xasCountNum;
        }

        public boolean isAschWk() {
            return isAschWk;
        }

        public void setAschWk(boolean aschWk) {
            isAschWk = aschWk;
        }

        public boolean isComment() {
            return isComment;
        }

        public void setComment(boolean comment) {
            isComment = comment;
        }

        public String getTimeQuantum() {
            return timeQuantum;
        }

        public void setTimeQuantum(String timeQuantum) {
            this.timeQuantum = timeQuantum;
        }

        public static class ActiveVotesBean {

            private double abs_rshares;
            private String voter;
            private long weight;
            private String rshares;
            private int percent;
            private String reputation;
            private String time;

            public double getAbs_rshares() {
                return abs_rshares;
            }

            public void setAbs_rshares(double abs_rshares) {
                this.abs_rshares = abs_rshares;
            }

            public String getVoter() {
                return voter;
            }

            public void setVoter(String voter) {
                this.voter = voter;
            }

            public long getWeight() {
                return weight;
            }

            public void setWeight(long weight) {
                this.weight = weight;
            }

            public String getRshares() {
                return rshares;
            }

            public void setRshares(String rshares) {
                this.rshares = rshares;
            }

            public int getPercent() {
                return percent;
            }

            public void setPercent(int percent) {
                this.percent = percent;
            }

            public String getReputation() {
                return reputation;
            }

            public void setReputation(String reputation) {
                this.reputation = reputation;
            }

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }
        }
    }
}
