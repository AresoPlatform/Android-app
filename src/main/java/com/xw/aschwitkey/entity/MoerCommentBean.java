package com.xw.aschwitkey.entity;

import java.util.List;

public class MoerCommentBean {

    private int post_id;
    private String author;
    private String permlink;
    private String category;
    private String title;
    private String body;
    private String json_metadata;
    private String created;
    private String last_update;
    private int depth;
    private int children;
    private long net_rshares;
    private String last_payout;
    private String cashout_time;
    private String total_payout_value;
    private String curator_payout_value;
    private String pending_payout_value;
    private String promoted;
    private int body_length;
    private long author_reputation;
    private String parent_author;
    private String parent_permlink;
    private String url;
    private String root_title;
    private String max_accepted_payout;
    private int percent_steem_dollars;
    private List<?> replies;
    private List<ActiveVotesBean> active_votes;
    private List<?> beneficiaries;

    public int getPost_id() {
        return post_id;
    }

    public void setPost_id(int post_id) {
        this.post_id = post_id;
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

    public String getJson_metadata() {
        return json_metadata;
    }

    public void setJson_metadata(String json_metadata) {
        this.json_metadata = json_metadata;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getLast_update() {
        return last_update;
    }

    public void setLast_update(String last_update) {
        this.last_update = last_update;
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

    public long getNet_rshares() {
        return net_rshares;
    }

    public void setNet_rshares(long net_rshares) {
        this.net_rshares = net_rshares;
    }

    public String getLast_payout() {
        return last_payout;
    }

    public void setLast_payout(String last_payout) {
        this.last_payout = last_payout;
    }

    public String getCashout_time() {
        return cashout_time;
    }

    public void setCashout_time(String cashout_time) {
        this.cashout_time = cashout_time;
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

    public String getPending_payout_value() {
        return pending_payout_value;
    }

    public void setPending_payout_value(String pending_payout_value) {
        this.pending_payout_value = pending_payout_value;
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

    public long getAuthor_reputation() {
        return author_reputation;
    }

    public void setAuthor_reputation(long author_reputation) {
        this.author_reputation = author_reputation;
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

    public List<?> getReplies() {
        return replies;
    }

    public void setReplies(List<?> replies) {
        this.replies = replies;
    }

    public List<ActiveVotesBean> getActive_votes() {
        return active_votes;
    }

    public void setActive_votes(List<ActiveVotesBean> active_votes) {
        this.active_votes = active_votes;
    }

    public List<?> getBeneficiaries() {
        return beneficiaries;
    }

    public void setBeneficiaries(List<?> beneficiaries) {
        this.beneficiaries = beneficiaries;
    }

    public static class ActiveVotesBean {
        private String voter;
        private String rshares;
        private String percent;
        private String reputation;

        public String getVoter() {
            return voter;
        }

        public void setVoter(String voter) {
            this.voter = voter;
        }

        public String getRshares() {
            return rshares;
        }

        public void setRshares(String rshares) {
            this.rshares = rshares;
        }

        public String getPercent() {
            return percent;
        }

        public void setPercent(String percent) {
            this.percent = percent;
        }

        public String getReputation() {
            return reputation;
        }

        public void setReputation(String reputation) {
            this.reputation = reputation;
        }
    }
}
