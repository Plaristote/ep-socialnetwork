class UserView extends View
  template: JST['user_show']
  name: 'inner-view'
  events:
    'click #edit-user': 'on_edit_user'
    'click #add-as-friend': 'on_add_as_friend'
    'click #delete-from-friends': 'on_delete_from_friends'

  constructor: (user) ->
    @user = user
    @listenTo @user,                    'change', @on_change.bind(@)
    @listenTo application.current_user, 'change', @on_current_user_change.bind(@)
    super null

  render: () ->
    @$el.html @template user: @user
    @on_current_user_change()
    super

  on_change: () ->
    @update_avatar()
    @update_fields()
    @on_friends_changed() if @user.has('friends_ids')

  update_avatar: () ->
    avatar_url = @user.get 'picture'
    @$('.avatar').attr 'src', avatar_url

  update_fields: () ->
    user = @user
    @$('[data-field]').each ->
      field_name = $(@).data 'field'
      value      = user.get field_name
      $('.value', @).text value

  on_current_user_change: () ->
    @$('#add-as-friend').hide()
    @$('#delete-from-friends').hide()
    if @user.get('id') == application.current_user.get('id')
      @$('#edit-user').show()
    else
      @$('#edit-user').hide()
      if application.current_user.has('friends_ids')
        if @user.get('id') in application.current_user.get('friends_ids')
          @$('#add-as-friend').hide()
          @$('#delete-from-friends').show()
        else
          @$('#add-as-friend').show()
          @$('#delete-from-friends').hide()

  on_add_as_friend: () ->
    application.current_user.add_friend @user.get('id')

  on_delete_from_friends: () ->
    application.current_user.delete_friend @user.get('id')

  on_edit_user: () ->
    Backbone.history.navigate '/users/update', trigger: true

  on_friends_changed: () ->
    @$(".friend-list").empty()
    @$(".friend-count").text @user.get('friends_ids').length
    for friend_id in @user.get('friends_ids')
      @$(".friend-list").append "<a href='#/users/#{friend_id}' data-friend='#{friend_id}'><span class='first-name'></span> <span class='last-name'></span></a>"
      friend = new User id: friend_id
      friend.fetch
        success: (friend) =>
          $friend_box = @$(".friend-list a[data-friend='#{friend.get 'id'}']")
          $('.first-name', $friend_box).text friend.get 'first_name'
          $('.last-name',  $friend_box).text friend.get 'last_name'
          $friend_box.css 'background-image', "url(#{friend.get 'picture'})"
