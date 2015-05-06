class PostView extends View
  template:      JST['posts_index']
  post_template: JST['post']
  name: 'post-index'

  initialize: () ->
    super
    @listenTo @get_user().posts,        'add',          @on_posts_changed.bind(@)
    @listenTo application.current_user, 'change',       @on_current_user_changed.bind(@)
    @listenTo @get_user(),              'change',       @on_user_changed.bind(@)

  get_user: () ->
    @_parent.user

  render: () ->
    @$el.html @template()
    @$('.send').click      => @send_post()
    @$('.load-more').click => @load_more_posts()
    super
    @get_user().posts.fetch()
    @

  load_more_posts: () ->
    @$('.load-more').fadeOut()
    @get_user().posts.getNextPage().done =>
      @$('.load-more').fadeIn() unless @get_user().posts.is_at_last_page()

  send_post: () ->
    post = new Post()
    post.set
      to:          @get_user().get 'id'
      url:         @$('#add-post [name="url"]').val()
      description: @$('#add-post [name="description"]').val()
    post.save {},
      success: =>
        @clear_form()
        @get_user().posts.add post

  clear_form: () ->
    @$('#add-post [name="url"]').val ''
    @$('#add-post [name="description"]').text ''

  on_current_user_changed: () ->
    @on_user_changed()

  on_user_changed: () ->
    @$('#add-post').hide()
    if @get_user().get('id') == application.current_user.get('id')
      @$('#add-post').show()
    else if @get_user().has 'friends_ids'
      current_user_id = application.current_user.get 'id'
      if current_user_id in @get_user().get 'friends_ids'
        @$('#add-post').show()

  on_posts_changed: () ->
    for post in @get_user().posts.models
      continue if (post.get('to')) != @get_user().get('id')
      @insert_post post unless @has_post(post.get 'id')

  on_post_enable: (event) ->
    post = @get_post_from_event event
    post.toggle_boolean_attribute 'enable',
      success: =>
        method = if post.enabled() then 'removeClass' else 'addClass'
        @$(".post[data-id='#{post.get 'id'}']")[method] 'disabled'
      error:   ->
        alert 'Could not toggle post visibility'

  on_post_highlight: (event) ->
    post = @get_post_from_event event
    post.toggle_boolean_attribute 'highlight',
      success: =>
        method = if post.highlighted() then 'addClass' else 'removeClass'
        @$(".post[data-id='#{post.get 'id'}']")[method] 'highlighted'
      error:   ->
        alert 'Could not toggle post highlighting'

  get_post_from_event: (event) ->
    $post   = $(event.currentTarget).closest('.post[data-id]')
    post_id = $post.data 'id'
    @get_user().posts.findWhere id: post_id

  insert_post: (post) ->
    if post.can_be_seen_by_current_user()
      $el = $(@post_template post: post)
      friend_box = new FriendBoxView $('.avatar', $el), post.get('from')
      friend_box.render()
      @$('#posts .list').append $el
      $('.enable-button, .disable-button', $el).click             (event) =>
        @on_post_enable event
      $('.highlight-button, .remove-highlight-button', $el).click (event) =>
        @on_post_highlight event
      @sort_posts()

  has_post: (id) ->
    @$("#posts .list .post[data-id='#{id}']").length != 0
    
  sort_posts: () ->
    $result = @$('#posts .list .post').sort (a, b) ->
      a_date = $(a).data 'created-at'
      b_date = $(b).data 'created-at'
      parseInt(a_date) < parseInt(b_date)
    $result.detach().appendTo @$('#posts .list')
