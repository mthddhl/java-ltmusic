local productId=ARGV[1]
local userId=ARGV[2]

local productCountKey='vip:product:count:' .. productId
local productBuyKey='vip:buy:user:' .. productId

if(tonumber(redis.call('get',productCountKey))<=0) then
    return 1
end

if(redis.call('sismember',productBuyKey,userId)==1) then
    return 2
end

redis.call('incrby',productCountKey,-1)
redis.call('sadd',productBuyKey,userId)

return 0