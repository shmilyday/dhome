$(function(){
	var Todo = Backbone.Model.extend({
		defaults:function(){
			return {title:"empty todo...",order:Todos.nextOrder(),done:false};
		},
		initialize:function(){
			if(!this.get("title"))
				this.set({"title":this.defaults.title});
		},
		toggle:function(){this.save({done:!this.get("done")});},
		clear:function(){this.destory();}
	});
	
	var TodoList = Backbone.Collection.extend({
		model:Todo,
		localStorage: new Store("todos-backbone"),
		done:function(){
			return this.filter(function(todo){
				return todo.get("done");
			});
		},
		remaining:function(){
			return this.without.apply(this,this.done());
		},
		nextOrder:function(){
			if(!this.length)
				return 1;
			return this.last().get("order") +1;
		},
		comparator:function(todo){
			return todo.get('order');
		}
	});
	
	var Todoview = Backbone.View.extend({
		tagName:"li",
		template:_.template($('#item-template').html()),
		events:{
			"click .toggle":"toggleDone",
			"dbclick .view":"edit",
			"click a.destory":"clear",
			"keypress .edit":"updateOnEnter",
			"blur .edit":"close"
		},
		initialize:function(){
			this.model.bind('change',this.render,this);
			this.model.bind('desotry',this.remove,this);
		},
		render:function(){
			this.$el.html(this.template(this.model.toJSON()));
			this.$el.toggleClass('done',this.model.get('done'));
			this.input = this.$('.edit');
			return this;
		},
		toggleDone:function(){
			this.model.toggle();
		},
		edit:function(){
			this.$el.addClass("editing");
			this.input.focus();
		},
		close:function(){
			var value = this.input.val();
			if(!value) this.clear();
			this.$el.removeClass("editing");
		},
		updateOnEnter:function(e){
			if(e.keyCode == 13) this.close(); 
		},
		clear:function(){
			this.model.clear();
		}
	});
	
	var AppView = Backbone.View.extend({
		el:$("#todoapp"),
		statusTempalte:_.template($("#stats-template").html()),
		events:{
			"keypress #new-todo":"createOnEnter",
			"click #clear-complated":"clearCompleted",
			"click #toggle-all":"toggleAllComplete"
		},
		initialize:function(){
			this.input = this.$("#new-todo");
			this.allCheckbox = this.$("#toggle-all")[0];
			Todos.bind('add',this.addOne,this);
			Todos.bind('reset',this.addAll,this);
			Todos.bind('all',this.render,this);
			this.footer = this.$('footer');
			this.main = $('#main');
			Todos.fetch();
		},
		render:function(){
			var done = Todos.done().length;
			var remaining = Todos.remaining().length;
			if(Todos.length){
				this.main.show();
				this.footer.show();
				this.footer.html(this.statsTemplate(
					{done:done,remaining:remaining}
				));
			}else{
				this.main.hide();
				this.footer.hide();
			}
			this.allCheckbox.checked = !remaining;
		},
		addOne:function(todo){
			var view = new TodoView({model:todo});
			this.$("#todo-list").append(view.render().el);
		},
		addAll:function(){
			Todos.each(this.addOne);
		},
		createOnEnter:function(e){
			if(e.keyCode!=13)return;
			if(!this.input.val())return;
			Todos.create({title:this.input.val()});
			this.input.val('');
		},
		clearCompleted:function(){
			_.each(Todos.done(),function(todo){
				todo.save({'done':done});
			});
		},
		toggleAllComplete:function(){
			var done = this.allCheckbox.checked;
			Todos.each(function(todo){
				todo.save({'done':done});
			});
		}
	});
	
	var App = new AppView;
});
