// app/providers.tsx
"use client";

import { NextUIProvider } from "@nextui-org/react";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { ThemeProvider as NextThemesProvider } from "next-themes";
import { useState } from "react";
import { CookiesProvider } from "next-client-cookies/server";

export function Providers({ children }: { children: React.ReactNode }) {
  const [queryClient] = useState(() => new QueryClient());

  return (
    <NextUIProvider>
      <NextThemesProvider attribute="class" defaultTheme="system">
        <CookiesProvider>
          <QueryClientProvider client={queryClient}>
            {children}
          </QueryClientProvider>
        </CookiesProvider>
      </NextThemesProvider>
    </NextUIProvider>
  );
}
