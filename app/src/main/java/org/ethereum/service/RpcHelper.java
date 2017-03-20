package org.ethereum.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hundsun.jresplus.testing.config.AutomationConfiguration;
import com.hundsun.jresplus.testing.engine.web.HttpHelper;
import com.hundsun.jresplus.testing.engine.web.HttpResponse;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;

public class RpcHelper {
	private static AtomicLong tx_ind = new AtomicLong(0);
	private static String httpProvider = AutomationConfiguration.getClientSettings().get("eth.rpc.root","http://testnet.51chain.net:8545");
	public static JSONObject call(String method, String... params) throws BusinessException
			  {
		HttpHelper httpClient = HttpHelper.getInstance();
		RPCRequest request = new RPCRequest(method, params);
		try {
	    HttpResponse response = httpClient.target(httpProvider)
					.addHeader("Content-Type", "application/json")
					.body(JSON.toJSONString(request)).post();
			if (response.getStatus() != 200) {
				throw BusinessException.error(BusinessException.errorHttpRpcFail,
						response.getContent());
			} else {
				String content = response.getContent();
				return JSON.parseObject(content);
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw BusinessException
					.error(BusinessException.errorHttpRpcFail, e.getMessage());
		}
	}
    /**
     * 主要用于调用需要指定key:value模式的 参数
     * referenced by http://ethereum.stackexchange.com/questions/3514/how-to-call-a-contract-method-using-the-eth-call-json-rpc-api
     * 例如：eth_call作为例子
     * JSONObject param=new JSONObject();
     * param.put("from",MY_ADDRESS);
     * param.put("to",contractAddress);
     * param.put("data","0x6d4ce63c");
     *
     * JSONArray ja=new JSONArray();
     * ja.add(param);
     * ja.add("latest");
     *
     * callRawJson("eth_call", ja);
     *
     * @param method
     * @param paramsArray
     * @return
     * @throws BusinessException
     */
    public static JSONObject callRawJson(String method, JSONArray paramsArray) throws BusinessException
    {
        JSONObject jo=new JSONObject();
        jo.put("id",tx_ind.getAndIncrement());
        jo.put("jsonrpc","2.0");
        jo.put("method",method);
        jo.put("params",paramsArray);
        HttpHelper httpClient = HttpHelper.getInstance();
        try {
            HttpResponse response = httpClient.target(httpProvider)
                    .addHeader("Content-Type", "application/json")
                    .body(jo.toJSONString()).post();
            if (response.getStatus() != 200) {
                throw BusinessException.error("error.http.rpc.fail",
                        response.getContent());
            } else {
                String content = response.getContent();
                return JSON.parseObject(content);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw BusinessException
                    .error("error.http.rpc.fail", e.getMessage());
        }
    }
	private static class RPCRequest {
		private String jsonrpc = "2.0";
		private String method;
		private String[] params;
		private long id;

		public RPCRequest(String method, String[] params) {
			super();
			this.id = tx_ind.getAndIncrement();
			this.method = method;
			if (params == null) {
				this.params = new String[0];
			} else {
				this.params = params;
			}
		}

		public String getJsonrpc() {
			return jsonrpc;
		}

		public void setJsonrpc(String jsonrpc) {
			this.jsonrpc = jsonrpc;
		}

		public String getMethod() {
			return method;
		}

		public void setMethod(String method) {
			this.method = method;
		}

		public String[] getParams() {
			return params;
		}

		public void setParams(String[] params) {
			this.params = params;
		}

		public long getId() {
			return id;
		}

		public void setId(long id) {
			this.id = id;
		}

	}
}
