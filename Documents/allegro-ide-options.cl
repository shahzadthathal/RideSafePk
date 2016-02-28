;;; Allegro CL IDE User Options

(in-package :cg-user) 

(ide.base::restore-configuration
    :allow-component-overlap t
    :ask-before-assuming-package nil
    :ask-before-auto-saving t
    :ask-for-action-at-ide-startup t
    :backtrace-frames-to-show 30
    :backtrace-safe-mode nil
    :cg-tree-font nil
    :class-graph-font nil
    :class-graph-initial-depth nil
    :close-inactive-listeners nil
    :close-project-close-editor-buffers nil
    :code-file-pretty-print-columns 90
    :comment-indent 55
    :compile-files-as-utf8 nil
    :context-sensitive-default-path t
    :debug-font nil
    :debug-history-limit 100
    :default-http-proxy
       (list nil nil nil nil)
    :dialog-print-array nil
    :dialog-print-circle t
    :dialog-print-length 12
    :dialog-print-level 3
    :directory-dialog-avoids-network nil
    :directory-dialog-searches-network-globally nil
    :display-console nil
    :display-extended-toolbar nil
    :display-form-grid t
    :display-status-bar t
    :display-toolbars t
    :distribution-directories nil
    :editor-font nil
    :editor-mode :host
    :editor-string-search-lines-of-padding 6
    :file-dialog-source-types
       (list (cons "Lisp files" "*.cl;*.lsp;*.lisp;*.jil")
             (cons "All files" "*"))
    :find-again-after-replace nil
    :find-in-files-file-list-portion 0.7
    :find-in-files-show-html-files-in-browser t
    :grid-x-spacing 8
    :grid-y-spacing 8
    :handle-multiple-packages-in-buffer t
    :highlight-selected-editor-tab t
    :ide-auto-font-changing nil
    :ide-dual-fonts-for-languages nil
    :ide-exterior
       (make-box 0 0 1266 578)
    :ide-html-history nil
    :ide-page-size
       (list 0 0)
    :ide-priority 8
    :ide-prompt nil
    :include-local-variables-in-backtraces nil
    :incremental-search nil
    :initial-package :cg-user
    :initial-search-directories nil
    :initial-search-filters nil
    :inspector-name-font nil
    :inspector-style :single-click-without-underscores
    :inspector-value-font nil
    :lisp-message-print-length 12
    :lisp-message-print-level 3
    :maximize-ide t
    :maximize-ide-background-window nil
    :maximum-symbol-completion-choices 25
    :min-pixels-between-widgets 4
    :most-recent-notified-express-version nil
    :move-ide-windows-on-screen-resize t
    :mozilla-library-path nil
    :new-project-create-form nil
    :new-project-show-editor t
    :new-project-show-form nil
    :new-project-show-project-manager t
    :open-files-in-gnu-emacs nil
    :open-project-action :compile
    :open-project-show-files-in-editor nil
    :open-project-show-project-manager t
    :patch-reminder-interval nil
    :patch-reminder-previous-time nil
    :pretty-printer :reindent
    :printer-font nil
    :profiler-included-node-types
       (list :self-hits :multiple-children :external-user-symbols
             :internal-user-symbols :external-lisp-symbols
             :external-cg-symbols :winapi-symbols)
    :project-parent-directory nil
    :query-exit t
    :query-os-exit nil
    :recent
       (list (list :open-project) (list :load-project) (list :open-file (make-pathname :host nil :device "C" :directory '(:absolute "Users" "shahzad" "Desktop") :name "Listener 1" :type "cl" :version :unspecific)) (list :load-file) (list :edit-definition))
    :recent-limit 18
    :run-project-action :save-compile
    :save-options-on-exit t
    :save-options-to-user-specific-file t
    :scroll-while-tracing nil
    :shift-windows-onto-screen-at-startup t
    :show-dialog-on-compiler-warnings t
    :show-widget-palette-when-click-form t
    :snap-to-components t
    :snap-to-grid nil
    :source-file-types
       (list "cl" "lsp" "lisp" "jil")
    :standard-toolbar-icons
       (list :new :open :save :save-all :load :gap :new-project
             :new-form :run-form :run-project :stop :gap :inspect
             :apropos :find :find-next :find-definitions :find-in-files
             :gap :macroexpand :incremental-compile
             :incremental-evaluation :gap :browse-class
             :graph-subclasses :gap :find-new-prompt :editor :gap
             :print :options :windows-help :gap :profile :unprofile-all
             :profile-results :gap :trace :untrace-all :trace-status
             :gap :breakpoint :unbreakpoint-all :breakpoint-status)
    :start-in-allegro-directory nil
    :symbol-completion-searches-all-packages t
    :use-antialiased-text-in-tree-graphs t
    :use-cg-html-browser nil
    :use-color-gradients-in-tree-graphs t
    :use-ide-background-window nil
    :use-ide-parent-window t
    :use-private-html-browser t
    :warn-on-no-action-taken t
    :window-configurations
       (list (list (list 1366 768) :options-dialog
                   (list :exterior (make-box 568 54 1134 487))
                   :extended-toolbar
                   (list :exterior (make-box 20 156 160 433))
                   :listener-frame
                   (list :exterior (make-box 0 35 953 583))))
    :center-all-modal-dialogs-on-screen :on-owner
    :cg-timer-interval 500
    :clipboard-history-limit 40
    :color-for-characters
       (make-rgb :red 190 :green 0 :blue 0)
    :color-for-comments dark-green
    :color-for-external-allegro-symbols dark-magenta
    :color-for-external-cg-symbols
       (make-rgb :red 200 :green 100 :blue 0)
    :color-for-external-cl-symbols
       (make-rgb :red 0 :green 0 :blue 180)
    :color-for-global-variables
       (make-rgb :red 240 :green 0 :blue 0)
    :color-for-strings
       (make-rgb :red 190 :green 0 :blue 0)
    :color-for-user-functions nil
    :colorize-on-load-file nil
    :colorize-on-typing t
    :conserve-indentation nil
    :custom-status-bar-font nil
    :custom-tooltip-font nil
    :default-height nil
    :default-height-factor 0.5
    :default-tab-height 21
    :default-tab-width 96
    :default-width nil
    :default-width-factor 0.75
    :delay-pop-up-menus-until-mouse-buttons-up nil
    :drag-images nil
    :external-format-for-saved-files :default
    :file-selection-buffer-size 13000
    :fixed-font
       (make-font-ex :modern "FixedSys" 13)
    :ignore-redundant-mouse-moves t
    :invoke-web-browsers-with-keystrokes
       (list :explorer)
    :load-utf8-if-no-bom nil
    :map-control-left-click-to-right-click nil
    :menu-tooltip-delay 1500
    :modal-dialog-margin 12
    :multi-picture-button-scroll-interval 200
    :offset-from-selected-window t
    :parenthesis-matching-color green
    :parenthesis-matching-style :color-block
    :pprint-plist-definers
       (list (list 'make-texture-info 1) (list 'open-menu 4)
             (list 'make-instance 2) (list 'make-window 2))
    :pprint-plist-pairs-on-separate-lines nil
    :private-html-browser-handle
       (list :chrome 263248 t)
    :proportional-font
       (make-font-ex nil "Segoe UI / Default" 12)
    :reserve-righthand-alt-key t
    :show-tooltips t
    :tooltip-delay 1000
    :tooltip-vertical-offset 16
    :use-cg-timer nil
    :use-mouse-clicks-to-copy-lisp-forms nil
    :use-pixmap-handles t
    :write-bom-to-utf8-files t)

;; End of file
