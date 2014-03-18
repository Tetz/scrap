def getUnreadTweets(tweets:List[Tweets],readTweets:List[MarkReadTweets],world_id:Int):List[Tweets] = {
    val diff = for(readTweet <- readTweets)yield {
      var tweetInfo = models.Tweets.fetchBy(readTweet.tweet_id)
      Tweets(
        readTweet.tweet_id,
        tweetInfo.head.user_id,
        world_id,
        tweetInfo.head.tweet
      )
    }
    val result = tweets.filter(!diff.contains(_))
    return result
  }

