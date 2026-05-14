#pragma once

#include <thread>
#include <atomic>
#include <cstdio>
#include <cstring>

#ifdef _WIN32
#include <windows.h>
#define SLEEP_SEC(s) Sleep((s) * 1000)
#else
#include <unistd.h>
#define SLEEP_SEC(s) sleep(s)
#endif

// C++ class with a Listener interface for callbacks.
// The scan() method runs a background thread that periodically
// invokes the listener's print() callback.
//
// Based on: https://habr.com/ru/articles/1017486/ (Part 2)
class NativeClass {
public:
    class Listener {
    public:
        virtual void print(const char* str) = 0;
        virtual ~Listener() {}
    };

private:
    std::atomic<bool> started{false};
    Listener* listener = nullptr;

    static void worker(NativeClass* nc) {
        int counter = 0;
        while (nc->started) {
            if (nc->listener != nullptr) {
                char msg[64];
                snprintf(msg, sizeof(msg), "From native: tick %d", counter++);
                nc->listener->print(msg);
            }
            SLEEP_SEC(1);
        }
        printf("[NativeClass] Worker thread finished\n");
        fflush(stdout);
    }

public:
    NativeClass() {
        printf("[NativeClass] created\n");
    }

    ~NativeClass() {
        stop();
        if (listener != nullptr) {
            delete listener;
            listener = nullptr;
        }
        printf("[NativeClass] destroyed\n");
    }

    void setListener(Listener* listener) {
        if (this->listener != nullptr) {
            delete this->listener;
        }
        this->listener = listener;
    }

    void scan() {
        started = true;
        std::thread t(worker, this);
        t.detach(); // detach so scan() doesn't block the calling thread
    }

    void stop() {
        started = false;
    }
};
