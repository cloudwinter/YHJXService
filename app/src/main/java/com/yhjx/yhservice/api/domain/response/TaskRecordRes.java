package com.yhjx.yhservice.api.domain.response;

import com.yhjx.yhservice.model.TaskOrder;

import java.io.Serializable;
import java.util.List;

public class TaskRecordRes implements Serializable {

    public List<TaskOrder> list;

    public int count;
}
