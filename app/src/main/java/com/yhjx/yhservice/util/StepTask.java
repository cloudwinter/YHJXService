package com.yhjx.yhservice.util;

/**
 * 步骤任务处理器
 * Created by xiayundong on 2018/5/17.
 */

public abstract class StepTask {

    /**
     * 总共步骤数目
     */
    private int mStepCount = 3;

    /**
     * 当前步骤
     */
    private int mCurStep = 0;

    /**
     * 开始
     */
    public boolean onStart() {
        return true;
    }

    /**
     * 结束
     */
    public void onFinish() {

    }


    /**
     * 第一步
     */
    public abstract void onStep1();

    /**
     * 第二步
     */
    public void onStep2() {

    }

    /**
     * 第三步
     */
    public void onStep3() {

    }

    /**
     * 执行下一步
     */
    public void executeNextStep() {
        if (mCurStep < mStepCount) {
            switch (mCurStep) {
                case 1:
                    mCurStep++;
                    onStep2();
                    break;
                case 2:
                    mCurStep++;
                    onStep3();

                    break;
                default:
                    break;
            }
        } else {
            onFinish();
        }
    }

    public void finishStep() {
        onFinish();
    }


    /**
     * 执行函数
     */
    public void execute() {
        if (onStart()) {
            mCurStep++;
            onStep1();

        }
    }


    /**
     * 执行函数
     * 最多只有3步
     */
    public void execute(int stepCount) {
        if (stepCount >= 3) {
            mStepCount = 3;
        } else {
            mStepCount = stepCount;
        }
        execute();
    }


}
