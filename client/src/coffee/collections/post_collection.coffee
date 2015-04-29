class PostCollection extends Collection
  model: Post 
  url: "/posts"
  resourceName: 'posts'

  set_user: (user) ->
    update_url = => @url = "/posts/user/#{user.get 'id'}"
    update_url()
    user.on 'change', update_url
    @on 'add', =>
      user.set 'posts', @models
      user.trigger 'change:posts'
