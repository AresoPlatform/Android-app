package com.xw.aschwitkey.entity;


import org.json.JSONObject;

import java.math.BigDecimal;

public class BbsUserBean {

    private Integer id;

    private String name;

    private BigDecimal totalValue;
    private BigDecimal canDrawValue;
    private BigDecimal canTakeValue;

    private BigDecimal spTotal;

    private BigDecimal balance;

    private BigDecimal sbd_balance;

    private BigDecimal reward_steem_balance;

    private BigDecimal reward_sbd_balance;

    private BigDecimal reward_vesting_balance;

    private BigDecimal delegated_vesting_shares;

    private JSONObject voting_manabar;

    private String json_metadata;

    private String created;

    private BigDecimal vesting_shares;

    private Long voting_power;

    private Boolean can_vote;

    private Integer sendPostNum;

    private String memoKey;


    private String userImage;

    private String vxName;

    private Integer userStatus;

    private Integer followNum;

    private Integer fansNum;

    private Integer voteNum;

    private Integer postNum;

    private BigDecimal rewardXas;

    private BigDecimal voteXas; 

    private BigDecimal frozenXas;

    private boolean isFollow;

    private String followName;

    private BigDecimal drawLimit;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(BigDecimal totalValue) {
        this.totalValue = totalValue;
    }

    public BigDecimal getCanDrawValue() {
        return canDrawValue;
    }

    public void setCanDrawValue(BigDecimal canDrawValue) {
        this.canDrawValue = canDrawValue;
    }

    public BigDecimal getCanTakeValue() {
        return canTakeValue;
    }

    public void setCanTakeValue(BigDecimal canTakeValue) {
        this.canTakeValue = canTakeValue;
    }

    public BigDecimal getSpTotal() {
        return spTotal;
    }

    public void setSpTotal(BigDecimal spTotal) {
        this.spTotal = spTotal;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getSbd_balance() {
        return sbd_balance;
    }

    public void setSbd_balance(BigDecimal sbd_balance) {
        this.sbd_balance = sbd_balance;
    }

    public BigDecimal getReward_steem_balance() {
        return reward_steem_balance;
    }

    public void setReward_steem_balance(BigDecimal reward_steem_balance) {
        this.reward_steem_balance = reward_steem_balance;
    }

    public BigDecimal getReward_sbd_balance() {
        return reward_sbd_balance;
    }

    public void setReward_sbd_balance(BigDecimal reward_sbd_balance) {
        this.reward_sbd_balance = reward_sbd_balance;
    }

    public BigDecimal getReward_vesting_balance() {
        return reward_vesting_balance;
    }

    public void setReward_vesting_balance(BigDecimal reward_vesting_balance) {
        this.reward_vesting_balance = reward_vesting_balance;
    }

    public BigDecimal getDelegated_vesting_shares() {
        return delegated_vesting_shares;
    }

    public void setDelegated_vesting_shares(BigDecimal delegated_vesting_shares) {
        this.delegated_vesting_shares = delegated_vesting_shares;
    }

    public JSONObject getVoting_manabar() {
        return voting_manabar;
    }

    public void setVoting_manabar(JSONObject voting_manabar) {
        this.voting_manabar = voting_manabar;
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

    public BigDecimal getVesting_shares() {
        return vesting_shares;
    }

    public void setVesting_shares(BigDecimal vesting_shares) {
        this.vesting_shares = vesting_shares;
    }

    public Long getVoting_power() {
        return voting_power;
    }

    public void setVoting_power(Long voting_power) {
        this.voting_power = voting_power;
    }

    public String getMemoKey() {
        return memoKey;
    }

    public void setMemoKey(String memoKey) {
        this.memoKey = memoKey;
    }

    public Boolean getCan_vote() {
        return can_vote;
    }

    public void setCan_vote(Boolean can_vote) {
        this.can_vote = can_vote;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getVxName() {
        return vxName;
    }

    public void setVxName(String vxName) {
        this.vxName = vxName;
    }

    public Integer getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(Integer userStatus) {
        this.userStatus = userStatus;
    }

    public Integer getFollowNum() {
        return followNum;
    }

    public void setFollowNum(Integer followNum) {
        this.followNum = followNum;
    }

    public Integer getFansNum() {
        return fansNum;
    }

    public void setFansNum(Integer fansNum) {
        this.fansNum = fansNum;
    }

    public Integer getVoteNum() {
        return voteNum;
    }

    public void setVoteNum(Integer voteNum) {
        this.voteNum = voteNum;
    }

    public Integer getPostNum() {
        return postNum;
    }

    public void setPostNum(Integer postNum) {
        this.postNum = postNum;
    }

    public BigDecimal getRewardXas() {
        return rewardXas;
    }

    public void setRewardXas(BigDecimal rewardXas) {
        this.rewardXas = rewardXas;
    }

    public BigDecimal getVoteXas() {
        return voteXas;
    }

    public void setVoteXas(BigDecimal voteXas) {
        this.voteXas = voteXas;
    }

    public Integer getSendPostNum() {
        return sendPostNum;
    }

    public void setSendPostNum(Integer sendPostNum) {
        this.sendPostNum = sendPostNum;
    }

    public BigDecimal getFrozenXas() {
        return frozenXas;
    }

    public void setFrozenXas(BigDecimal frozenXas) {
        this.frozenXas = frozenXas;
    }

    public boolean isFollow() {
        return isFollow;
    }

    public void setFollow(boolean follow) {
        isFollow = follow;
    }

    public String getFollowName() {
        return followName;
    }

    public void setFollowName(String followName) {
        this.followName = followName;
    }

    public BigDecimal getDrawLimit() {
        return drawLimit;
    }

    public void setDrawLimit(BigDecimal drawLimit) {
        this.drawLimit = drawLimit;
    }
}
