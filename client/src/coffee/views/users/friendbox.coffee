class FriendBoxView extends Backbone.View
  constructor: ($container, friend_id) ->
    @$container = $container
    @friend_id  = friend_id

  render: () ->
    html  = "<a href='#/users/#{@friend_id}' data-friend='#{@friend_id}'>"
    html +=   "<span class='first-name'></span> "
    html +=   "<span class='last-name'></span>"
    html += "</a>"
    @$el  = $(html)
    @$container.append @$el
    application.user_cache.find_or_fetch { id: @friend_id },
      success: @on_user_received.bind(@)
      error:   => console.log "Failed to fetch friend #{@friend_id}"
    @

  on_user_received: (user) ->
    @user = user
    @listenTo @user, 'change', @update_user_data.bind(@)
    @update_user_data() if @user.has 'picture'

  update_user_data: () ->
    @$('.first-name').text @user.get 'first_name'
    @$('.last-name').text  @user.get 'last_name'
    @$el.css 'background-image', "url(#{@user.get 'picture'})"

  $: (selector) ->
    $(selector, @$el)