class UserView extends View
  template: JST['user_show']
  name: 'inner-view'
  events:
    'click #edit-user': 'on_edit_user'

  constructor: (user) ->
    @user = user
    @listenTo @user,                    'change', @on_change.bind(@)
    @listenTo application.current_user, 'change', @on_current_user_change.bind(@)
    super null

  render: () ->
    console.log 'user ids'
    console.log @user.get('id')
    console.log application.current_user.get('id')
    @$el.html @template user: @user
    @on_current_user_change()
    super

  on_change: () ->
    @update_avatar()
    @update_fields()

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
    if @user.get('id') == application.current_user.get('id')
      @$('#edit-user').show()
    else
      @$('#edit-user').hide()

  on_edit_user: () ->
    Backbone.history.navigate '/users/update', trigger: true