--
-- Created by IntelliJ IDEA.
-- User: suys
-- Date: 19-1-20
-- Time: 下午3:54
-- To change this template use File | Settings | File Templates.
--

-- checkandset.lua
local current = redis.call('GET', KEYS[1])
if current == ARGV[1]
then redis.call('SET', KEYS[1], ARGV[2])
    return true
end
return false