package com.zoke.neb.activities;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.zoke.neb.base.BaseActivity;

import org.xutils.common.util.LogUtil;

import java.util.Arrays;

import io.nebulas.Constants;
import io.nebulas.api.SmartContracts;
import io.nebulas.model.ContractModel;
import io.nebulas.model.GoodsModel;
import io.nebulas.utils.Util;

/**
 * Created by wulijie on 2018/6/4.
 */
public abstract class BaseNebActivity extends BaseActivity {


    //每次请求对应的类似于请求码的一类东西
    private String serialNumber = "";
    private boolean isDoing = false;//判断是否正在执行某个请求的开关
    private SmartCallback callback;//切记 一个页面只能一次请求一个call接口


    /**
     * 合约地址
     *
     * @return
     */
    public abstract String getSmartContractsAddress();

    /**
     * 执行合约消费的nas？
     *
     * @return
     */
    public abstract String getValue();


    /**
     * 是否在线上执行
     *
     * @return
     */
    public abstract boolean isMainnet();

    /**
     * call no call back 需要配合trans查询接口
     *
     * @param functionName 调用合约中的方法名
     * @param args         合约方法需要传的参数
     * @param smartAddress 合约地址
     * @param value        执行合约需要消耗的nas
     */
    public void post(@NonNull String functionName, @NonNull String[] args, @NonNull String smartAddress, @NonNull String value, @NonNull SmartCallback callback) {
        //初始化请求码
        int main_net = isMainnet() ? Constants.MAIN_NET : Constants.TEST_NET;
        this.callback = callback;
        serialNumber = Util.getRandomCode(Constants.RANDOM_LENGTH);
        isDoing = true;
        callback.onStart();
        LogUtil.e("do call start =>" + serialNumber);
        GoodsModel goods = new GoodsModel();
        goods.name = functionName;
        goods.desc = functionName;
        SmartContracts.call(this,
                main_net, goods,
                functionName, smartAddress,
                value, args,
                serialNumber);
    }

    /**
     * call without callback need - trans getStatus
     *
     * @param functionName
     * @param args
     * @param callback
     */
    public void post(@NonNull String functionName, @NonNull String[] args, @NonNull SmartCallback callback) {
        post(functionName, args, getSmartContractsAddress(), getValue(), callback);
    }


    /**
     * 从星云链获取数据
     *
     * @param functionName 合约中的方法
     * @param args         合约中的方法参数
     * @param userAddress  用户的address
     * @param callback
     */
    public void get(String functionName, Object[] args, String userAddress, @NonNull final SmartCallback callback) {
        if (args == null)
            args = new Object[]{};
        callback.onStart();
        ContractModel contractModel = new ContractModel();
        contractModel.args = Arrays.toString(args);//notice: Arrays.toString() was need , if not : {"error":"json: cannot unmarshal array into Go value of type string"}
        contractModel.function = functionName;
        SmartContracts.call(contractModel, userAddress, getSmartContractsAddress(), 1, new SmartContracts.StatusCallback() {
            @Override
            public void onSuccess(final String response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        LogUtil.e("onSuccess :" + response);
                        callback.onSuccess(response);
                    }
                });
            }

            @Override
            public void onFail(final String error) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        LogUtil.e("onFail " + error);
                        callback.onError(error);
                    }
                });
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (isDoing) {
            nasQueryTransferStatus();
        }
    }


    //call back 接口 可以直接返回数据

    /**
     * 封装返回结果
     */
    public interface SmartCallback {
        void onStart();

        void onSuccess(String response);

        void onError(String error);
    }


    /**
     * 获取交易状态-依据 serialNumber
     */
    private void nasQueryTransferStatus() {
        isDoing = false;
        if (TextUtils.isEmpty(serialNumber)) {
            callback.onError("serialNumber is empty");
            return;
        }
        int main_net = isMainnet() ? Constants.MAIN_NET : Constants.TEST_NET;
        SmartContracts.queryTransferStatus(main_net, serialNumber, new SmartContracts.StatusCallback() {
            @Override
            public void onSuccess(final String response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        LogUtil.e("onSuccess :" + response);
                        callback.onSuccess(response);
                    }
                });
            }

            @Override
            public void onFail(final String error) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        LogUtil.e("onFail " + error);
                        callback.onError(error);
                    }
                });
            }
        });
    }

    /**
     * 获取某一个合约地址的状态信息
     */
    public void getSmartContractsInfo(String smartAddress, @NonNull final SmartCallback callback) {
        SmartContracts.queryAccountState(smartAddress, new SmartContracts.StatusCallback() {
            @Override
            public void onSuccess(final String response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        LogUtil.e("onSuccess :" + response);
                        callback.onSuccess(response);
                    }
                });
            }

            @Override
            public void onFail(final String error) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        LogUtil.e("onFail " + error);
                        callback.onError(error);
                    }
                });
            }
        });
    }


    /**
     * @param toAddress 入账钱包地址，钱包地址，钱包地址
     * @param callback  回调
     */
    public void nasPay(String toAddress, @NonNull SmartCallback callback) {
        isDoing = true;
        this.callback = callback;
        int main_net = isMainnet() ? Constants.MAIN_NET : Constants.TEST_NET;
        callback.onStart();
        serialNumber = Util.getRandomCode(Constants.RANDOM_LENGTH);
        GoodsModel goods = new GoodsModel();
        goods.name = "dapp-store";
        goods.desc = "dapp-store";
        SmartContracts.pay(this, main_net, goods, toAddress, getValue(), serialNumber);
    }

}
