class User extends Backbone.Model
  urlRoot: "/users"

  initialize: () ->
    @posts = new PostCollection()
    @posts.set_user @

  fetch: () ->
    application.user_cache.add @
    @fetch_friends()
    super

  add_friend: (friend_id) ->
    $.ajax
      method:   'POST'
      url:      '/friends'
      data:     JSON.stringify friend: friend_id
      dataType: 'json'
      success: () =>
        array = @get('friends_ids') || []
        array.push friend_id
        @set 'friends_ids', array
        @trigger 'change'
      error: () =>
        console.log "Could not add friend #{friend_id} to user", @

  delete_friend: (friend_id) ->
    $.ajax
      method: 'DELETE'
      url:    "/friends/user/#{friend_id}"
      success: () =>
        array = @get('friends_ids') || []
        @set 'friends_ids', _.without(array, friend_id)
      error: () =>
        console.log "Could not remove friend #{friend_id} from user", @

  fetch_friends: () ->
    $.ajax
      method: 'GET'
      url:    "/friends/user/#{@get 'id'}"
      success: (data) => @set 'friends_ids', data.friends
      error:   ()     => console.log "Could not fetch friends for user", @