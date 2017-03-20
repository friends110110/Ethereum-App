/**
 * 注意：浏览器中测试需要将触发事件的地方注释掉
 * @title 代币合约(只存储余额不为0的用户的账户信息)
 */
contract HsCoinContract {
  address[]  admins;//管理员
  mapping(uint=>address) addVotes;//增加某个管理员的投票，key：admins数组的索引，value：待增加的管理员的地址
  mapping(uint=>address) removeVotes;//踢出某个管理员的投票，key：admins数组的索引，value：待踢出的管理员的地址

  uint public issueAmount;//发行的代币总量

  uint public redeemAmount;//已经兑换的代币数量

  bool public paused;//合约是否被暂停

  uint nextId=1;//下一个用户Id

  mapping(address=>uint) public balanceOf;//账户和可用余额的对应关系

  mapping(address=>uint) public frozenAmountOf;//账户和冻结金额的对应关系

  mapping(address=>bool) public lockedAccount;//已经锁定的账户

  event IssuseMade(address to,uint amount);//向指定账户发放代币事件
  event TransferMade(address from,address to,uint amount);//转账事件
  event ExchangeGoodsMade(address from,uint amount);//兑换物品事件
  event ChangeAddressMade(address oldAddr,address newAddr);//更换储户账号
  event AddAdminVoteMade(address voter,address candidate);//增加管理员投票事件
  event AdminAddedMade(address candidate);//增加了新的管理员（过半数用户投票同意）
  event RemoveAdminVoteMade(address voter,address target);//删除管理员投票事件
  event AdminRemovedMade(address target);//删除了管理员（过半数用户投票同意）
  event DeductBalanceMade(address target,uint amount);//扣除账户余额

  /**
   * 只允许管理员进行的操作
   */
  modifier onlyAdmin{
      bool isAdmin=false;
      for(uint i=0;i<admins.length;i++){
          if(msg.sender==admins[i]){
              isAdmin=true;
              break;
          }
      }
      if(!isAdmin){
          throw;
      }
      _
  }

  /**
   * 账户未锁定
   */
  modifier notLocked(address account){
      if(lockedAccount[account]){
          throw;
      }
      _
  }

  /**
   * 余额足够
   */
  modifier balanceEnough(address account,uint amount){
      if(balanceOf[account]<amount){
          throw;
      }
      _
  }

  /**
   * 合约有效
   */
  modifier onlyActive(){
      if(paused){
          throw;
      }
      _
  }
  string public name;
  string public symbol;
  uint8 public decimals;
  uint256 public totalSupply;
  /**
   * 合约构造函数,初始管理员人数不确定，可拓展
   */
  function HsCoinContract( uint256 initialSupply,
        string tokenName,
        uint8 decimalUnits,
        string tokenSymbol){
    issueAmount=initialSupply;
    name=tokenName;
    symbol=tokenSymbol;
    decimals=decimalUnits;
  }

  /**
  *设置管理员地址
  */
  function initAdminAddress(address[] eAdmins){
    for(var i=0;i<eAdmins.length;i++){
      admins.push(eAdmins[i]);
    }
  }
  /**
   * 管理员向用户发放代币
   * @param target 接收方账号
   * @param amount 发放的代币数量
   */
  function issuse(address target,uint amount) onlyActive onlyAdmin notLocked(target){
      balanceOf[target]+=amount;
      issueAmount+=amount;
      IssuseMade(target,amount);
  }

  /**
   *用户之间的转账
   * @param to 接收方账户
   * @param amount 转账金额
   */
  function transfer(address to,uint amount) onlyActive notLocked(msg.sender)  notLocked(to) balanceEnough(msg.sender,amount) public{
     balanceOf[msg.sender]-=amount;
     balanceOf[to]+=amount;
     TransferMade(msg.sender,to,amount);
  }

  /**
   *扣除余额（仅在没有商品兑换的情况下收回已发放的代币），管理员发错代币收回代币所用
   * @param target 目标账户
   * @param amount 扣除金额
   */
  function deductBalance(address target,uint amount)  onlyActive onlyAdmin notLocked(target) balanceEnough(target,amount){
       balanceOf[target]-=amount;
       issueAmount-=amount;
       DeductBalanceMade(target,amount);
  }

  /**
   * 兑换物品
   * @param amount 金额
   */
  function exchangeGoods(uint amount) onlyActive notLocked(msg.sender)  balanceEnough(msg.sender,amount) public{
      balanceOf[msg.sender]-=amount;
      redeemAmount+=amount;
      ExchangeGoodsMade(msg.sender,amount);
  }

  /**
   * 更换储户账号地址：将旧地址对应的代币余额转移到新地址，并锁定旧地址
   */
  function changeAddress(address oldAddr,address newAddr) onlyActive notLocked(oldAddr) notLocked(newAddr) public{
      balanceOf[newAddr]+=balanceOf[oldAddr];
      frozenAmountOf[newAddr]+=frozenAmountOf[oldAddr];
      balanceOf[oldAddr]=0;
      frozenAmountOf[oldAddr]=0;
      lockedAccount[oldAddr]=true;
      ChangeAddressMade(oldAddr,newAddr);
  }

  /**
   * 增加管理员
   * @param candidate 候选人
   */
  function addAdmin(address candidate) {
       uint index=admins.length+10;//操作人索引（0~admins.length-1说明操作人是管理员）
       bool exists=false;//候选人是否已经是管理员
       for(uint i=0;i<admins.length;i++){
          if(msg.sender==admins[i]){
              index=i;
          }
          if(admins[i]==candidate){
              exists=true;
          }
       }

       if(index>=admins.length||exists){//如果操作人不是管理员或者候选人本身就是管理员，则抛异常
           throw;
       }

       addVotes[index]=candidate;
       uint votes=0;
       for(uint j=0;j<admins.length;j++){
           if(addVotes[j]==candidate){
               votes++;
           }
       }
       if(votes*2>admins.length){//投票成功
           for(var k=0;k<admins.length;k++){
               addVotes[k]=0x0;//覆盖数据，避免delete带来的消耗
           }
           admins.push(candidate);       //admins 添加新成员， length也会增加
           AdminAddedMade(candidate);
       }else{//通知其他人投票
           AddAdminVoteMade(msg.sender,candidate);
       }
  }

   /**
   * 移除管理员
   * @param target 待移除的管理员
   */
  function removeAdmin(address target) {//管理员发起的
       if(admins.length==1){
           throw;
       }
       uint index=admins.length+10;//操作人索引（0~admins.length-1说明操作人是管理员）
       uint targetIndex=admins.length+10;//待移除的管理员的索引（0~admins.length-1说明是管理员）
       for(uint i=0;i<admins.length;i++){
          if(msg.sender==admins[i]){
              index=i;
          }
          if(admins[i]==target){
              targetIndex=i;
          }
       }
       if(index>=admins.length||targetIndex>=admins.length){
           throw;
       }
       removeVotes[index]=target;
       uint votes=0;
       for(uint j=0;j<admins.length;j++){
           if(removeVotes[j]==target){
               votes++;
           }
       }
       if(votes*2>admins.length){//投票成功
           for(var k=0;k<admins.length;k++){
               removeVotes[k]=0x0;
           }
           //将最后一个管理员的账户及增加管理员投票信息赋值到删除的位置
           admins[targetIndex]=admins[admins.length-1];
           addVotes[targetIndex]=addVotes[admins.length-1];//将最后一位的admins的索引 赋给被删除的那位
           admins.length-=1;    //admins 元素长度-1
           AdminRemovedMade(target);
       }else{
           RemoveAdminVoteMade(msg.sender,target);
       }
  }

  function getAdminCount() constant returns(uint){
      return admins.length;
  }

  function getAdmin(uint index) constant returns(address){
      return admins[index];
  }

  /**
   *暂停代币合约（禁止所有写操作）
   */
  function pause() onlyAdmin{
      paused=true;
  }

  /**
   *恢复代币合约
   */
   function resume() onlyAdmin{
       paused=false;
   }

  /**
   * 销毁合约(测试时使用，正式上线要去掉)
   */
  function destroy(){
      if(msg.sender!=tx.origin){
          throw;
      }
      selfdestruct(msg.sender);
  }

}
